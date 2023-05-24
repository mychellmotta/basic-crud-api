package com.mychellmotta.basiccrudapi.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class Utils {

    public static File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(
                requireNonNull(multipartFile.getOriginalFilename())
                            );

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }
}
