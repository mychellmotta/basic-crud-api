package com.mychellmotta.basiccrudapi.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class Utils {

    public static File convertMultiPartToFile(MultipartFile multipartfile) throws IOException {
        File file = new File(
                requireNonNull(multipartfile.getOriginalFilename())
                    );
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartfile.getBytes());
        fos.close();
        return file;
    }
}
