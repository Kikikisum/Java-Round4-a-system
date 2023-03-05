package com.server.Controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.server.Entity.SysItem;
import com.server.Entity.SysUser;
import com.server.Mapper.SysItemMapper;
import com.server.Mapper.SysUserMapper;
import com.server.Param.Result;
import com.server.Service.OssService;
import com.server.Service.SysItemService;
import com.server.Service.SysUserService;
import com.server.Utils.JwtUtils;
import com.server.Utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static cn.hutool.core.io.FileUtil.getInputStream;

@RestController
@RequestMapping("/sys/item")
public class UserItemController {

    @Autowired
    SysItemService sysItemService;

    @Autowired
    SysItemMapper sysItemMapper;
    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    OssService ossService;

    /**
     * 用户添加一个众筹项目的接口
     * @param item 项目信息
     * @param file 上传文件
     * @return
     */
    @PostMapping("/add")
    public Result addItem(SysItem item, @RequestParam("file") MultipartFile file) throws UnsupportedEncodingException {
        String newFileName;
        //存入数据库，这里可以加if判断
        SysItem sysItem = new SysItem();
        sysItem.setTelephone(item.getTelephone());
        sysItem.setStatus(0);
        sysItem.setPrice(0);
        sysItem.setTitle(item.getTitle());
        sysItem.setContent(item.getContent());
        sysItem.setUserid(item.getUserid());
        if (sysItem.getTitle()=="")
            return Result.retInfo(10009,"标题不能为空",MapUtil.builder().map());
        if (sysItem.getUserid()==0)
            return Result.retInfo(10010,"所属人不能为空",MapUtil.builder().map());
        if(sysItem.getContent()=="")
            return Result.retInfo(10011,"内容不能为空",MapUtil.builder().map());
        if(sysItem.getTelephone()=="")
            return Result.retInfo(10012,"电话不能为空",MapUtil.builder().map());
        if (file.isEmpty())
        {
            sysItemService.addOneItem(sysItem);
            return Result.retInfo(10013,"未发送图片",MapUtil.builder().map());
        }
        else
        {

            String fileName = file.getOriginalFilename();
            //拼接url，采用随机数，保证每个图片的url不同
            UUID uuid = UUID.randomUUID();
            //重新拼接文件名
            int index = fileName.indexOf(".");
            newFileName =fileName.replace(".","")+uuid+fileName.substring(index);

            try {
                //将文件保存指定目录
                ossService.uploadFile(file.getInputStream(),newFileName);

            } catch (Exception e) {
                e.printStackTrace();
            }
            newFileName = ossService.downloadFile(newFileName);
            sysItem.setImageurl(newFileName);
            sysItemService.addOneItem(sysItem);
        }
        return Result.retInfo(10000,"保存成功",MapUtil.builder()
                .put("records",newFileName)
                .map());
    }

    /**
     * 修改众筹项目信息
     * @param item
     * @param file
     * @return
     */
    @PostMapping("/update")
    public Result updateItem(SysItem item, @RequestParam("file") MultipartFile file){

        //存入数据库，这里可以加if判断
        LambdaQueryWrapper<SysItem> lqw1 = new LambdaQueryWrapper<SysItem>();
        lqw1.eq(SysItem::getId,item.getId());
        SysItem sysItem= sysItemMapper.selectOne(lqw1);
        if (file.isEmpty())
        {
            sysItemMapper.updateById(item);
            return Result.retInfo(10008,"未发送图片",MapUtil.builder().map());
        }
        else
        {
            ossService.deletePhoto(sysItem.getImageurl());
            String fileName = file.getOriginalFilename();
            //拼接url，采用随机数，保证每个图片的url不同
            UUID uuid = UUID.randomUUID();
            //重新拼接文件名
            int index = fileName.indexOf(".");

            String newFileName =fileName.replace(".","")+uuid+fileName.substring(index);
            item.setImageurl(newFileName);
            sysItemMapper.updateById(item);
            try {
                //将文件保存指定目录
                ossService.uploadFile(file.getInputStream(),newFileName);
                newFileName = ossService.downloadFile(newFileName);

            } catch (Exception e) {
                e.printStackTrace();
            }
            sysItem.setImageurl(newFileName);
            sysItemService.addOneItem(sysItem);
        }
        return Result.succ("众筹信息修改成功");
    }
    /**
     * stream转byte
     * @param inputStream
     * @return
     */
    public byte[]  getBytesByStream(InputStream inputStream){
        byte[] bytes = new byte[1024];

        int b;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            while((b = inputStream.read(bytes)) != -1){

                byteArrayOutputStream.write(bytes,0,b);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回众筹项目
     * @return
     */
    @PostMapping("/listItem")
    public List<SysItem> listItem() throws UnsupportedEncodingException {
        LambdaQueryWrapper<SysItem> lqw1 = new LambdaQueryWrapper<SysItem>();
        lqw1.eq(SysItem::getStatus,1);
        List<SysItem> sysItems = sysItemMapper.selectList(lqw1);
//        for (SysItem item:sysItems) {
//            if (item.getImageurl()!=null)
//                item.setImageurl(ossService.downloadFile(item.getImageurl()));
//        }
        return sysItems;
    }

    @PostMapping("/verify")
    public Result examineItem(@RequestBody Map<String,String> map){
        LambdaQueryWrapper<SysItem> lqw1 = new LambdaQueryWrapper<SysItem>();
        lqw1.eq(SysItem::getId,Integer.valueOf(map.get("item_id")));
        SysItem item = sysItemMapper.selectOne(lqw1);
        item.setStatus(Integer.valueOf(map.get("status")));
        sysItemMapper.updateById(item);
        return Result.succ("修改");
    }
    @PostMapping("/viewItem/{status}")
    public List<SysItem> viewItem(@PathVariable("status") int status) throws UnsupportedEncodingException {
        LambdaQueryWrapper<SysItem> lqw1 = new LambdaQueryWrapper<SysItem>();
        lqw1.eq(SysItem::getStatus,status);
        List<SysItem> sysItems = sysItemMapper.selectList(lqw1);
//        for (SysItem item:sysItems) {
//            if (item.getImageurl()!=null)
//                item.setImageurl(ossService.downloadFile(item.getImageurl()));
//        }
        return sysItems;
    }

    @PostMapping("find")
    public Object findItem(@RequestParam("title") String title)
    {
        if (title==""||title==null||title.isEmpty())
            return MapUtil.builder()
                    .put("code","100023").put("msg","标题为空").put("data",MapUtil.builder().map())
                    .map();
        LambdaQueryWrapper<SysItem> lqw1 = new LambdaQueryWrapper<SysItem>();
        lqw1.like(SysItem::getTitle,title);
        List<SysItem> sysItems = sysItemMapper.selectList(lqw1);
        if (sysItems.isEmpty())
            return MapUtil.builder()
                    .put("code","100024").put("msg","未查询到").put("data",MapUtil.builder().map())
                    .map();
        return sysItems;
    }
    @PostMapping("deletelist")
    public Result deleteItem(@RequestBody Map<String,String> map)
    {
        List<Integer> list= new ArrayList<>();
        for (String value : map.values()) {
            list.add(Integer.valueOf(value));
        }
        if (list.size()!=0)
        {
            sysItemMapper.deleteBatchIds(list);
            return Result.succ("批量删除成功");
        }
        else
            return Result.retInfo(100021,"删除项目数目为0",MapUtil.builder().map());
    }
    @PostMapping("getmyitem/{id}")
    public List<SysItem> getMyItem(@PathVariable("id") int id)
    {
        LambdaQueryWrapper<SysItem> lqw1 = new LambdaQueryWrapper<SysItem>();
        lqw1.eq(SysItem::getUserid,id);
        List<SysItem> sysItems = sysItemMapper.selectList(lqw1);
        return sysItems;
    }
}
