package com.blogwebapi.service.impl;

import com.blogwebapi.service.ICloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService implements ICloudinaryService {
    @Autowired
    private Cloudinary cloudinary;
    @Override
    public String uploadImage(MultipartFile multipartFile, String folder) {
        Map<String, String> result = null;
        try{
            Map<String, Object> params = Map.of(
                    "transformation", new Transformation().fetchFormat("auto"),
                    "folder", "blogwebapi/" + folder
            );
            result = cloudinary.uploader().upload(multipartFile.getBytes(), params);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.get("url");
    }
}
