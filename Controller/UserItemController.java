package com.server.Controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.server.Entity.SysItem;
import com.server.Entity.SysUser;
import com.server.Mapper.SysItemMapper;
import com.server.Param.Result;
import com.server.Service.SysItemService;
import com.server.Service.SysUserService;
import com.server.Utils.JwtUtils;
import com.server.Utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    //存放图片的地址
    @Value("${file.upload-path}")
    private String pictureurl;
    /**
     * 用户添加一个众筹项目的接口
     * @param map
     * @return
     */
    @PostMapping("/add/{id}")
    public Result addItem(@PathVariable("id") int id,@RequestBody Map<String,String> map)
    {
        SysItem item = new SysItem();
        item.setIdentity(map.get("identity"));
        item.setAudit_status(0);
        item.setPrice(Integer.valueOf(map.get("price")));
        item.setTitle(map.get("title"));
        item.setUser_id(id);
        item.setContent(map.get("content"));
        item.setStatus(1);
        int itemID = sysItemService.addOneItem(item);
        LambdaQueryWrapper<SysItem> lqw1 = new LambdaQueryWrapper<SysItem>();
        lqw1.eq(SysItem::getId,itemID);
        SysItem sysItem= sysItemMapper.selectOne(lqw1);
        return Result.succ(MapUtil.builder()
                .put("records",sysItem)
                .map());
    }

    /**
     * 上传图片数据
     * @param id 众筹项目的ID
     * @param file 图片
     * @return
     */
    @PostMapping("/setting/{id}")
    public Result setImg(@PathVariable("id") int id, @RequestBody MultipartFile file){
        String fileName = file.getOriginalFilename();
        File saveFile = new File(pictureurl);
        if (!saveFile.exists()){
            saveFile.mkdir();
        }
        //拼接url，采用随机数，保证每个图片的url不同
        UUID uuid = UUID.randomUUID();
        //重新拼接文件名
        int index = fileName.indexOf(".");
        String newFileName =fileName.replace(".","")+uuid+fileName.substring(index);
        //存入数据库，这里可以加if判断
        SysItem sysItem = new SysItem();
        sysItem.setId(id);
        sysItem.setImageurl(newFileName);
        int userID = sysItemMapper.updateById(sysItem);
        try {
            //将文件保存指定目录
            file.transferTo(new File(pictureurl + "/"+newFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("保存成功");
        return Result.succ(MapUtil.builder()
                .put("userID",userID)
                .map());
    }

    /**
     * 用户修改自己提交的众筹项目
     * @param map
     * @return
     */
    @PostMapping("/update")
    public Result updateItem(@RequestBody Map<String,String> map)
    {
        SysItem sysItem = new SysItem();
        sysItem.setTitle(map.get("title"));
        sysItem.setId(Integer.valueOf(map.get("id")));
        sysItem.setIdentity(map.get("identity"));
        sysItem.setContent(map.get("content"));
        sysItem.setPrice(Integer.valueOf(map.get("price")));
        sysItemMapper.updateById(sysItem);
        return Result.succ("众筹信息修改成功");
    }

    /**
     * 获取对应ID的图像
     * @param id
     * @return
     */
    @PostMapping("/retpic/{id}")
    public ResponseEntity<byte[]> getImg(@PathVariable("id") int id) {
        LambdaQueryWrapper<SysItem> lqw1 = new LambdaQueryWrapper<SysItem>();
        List<SysItem> users = sysItemMapper.selectList(lqw1);
        lqw1.eq(SysItem::getId,id);
        SysItem sysItem= sysItemMapper.selectOne(lqw1);
        String path;
        if (sysItem.getImageurl().equals(""))
            path = pictureurl+"blank.jpg";
        else
            path = pictureurl+sysItem.getImageurl();
        //通过自己写的http工具类获取到图片输入流
        InputStream inputStream =  getInputStream(path);
        //将输入流转为byte[]
        byte[] bytesByStream = getBytesByStream(inputStream);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(bytesByStream,headers, HttpStatus.OK);
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
    public List<SysItem> listItem(){
        LambdaQueryWrapper<SysItem> lqw1 = new LambdaQueryWrapper<SysItem>();
        List<SysItem> sysItems = sysItemMapper.selectList(lqw1);

        return sysItems;
    }

    @PostMapping("/verify")
    public void examineItem(@RequestBody Map<String,String> map){
        LambdaQueryWrapper<SysItem> lqw1 = new LambdaQueryWrapper<SysItem>();
        lqw1.eq(SysItem::getId,Integer.valueOf(map.get("item_id")));
        SysItem item = sysItemMapper.selectOne(lqw1);
        item.setAudit_status(Integer.valueOf(map.get("status")));
        sysItemMapper.updateById(item);
    }




}
