package com.mychellmotta.basiccrudapi.service;

import com.mychellmotta.basiccrudapi.dto.ThingSheetDto;
import com.poiji.bind.Poiji;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
public class FileService {

    public List<ThingSheetDto> processExcelData(MultipartFile multipartFile) {
        File file = null;
        try {
            file = convertMultipartToFile(multipartFile);
            return Poiji.fromExcel(file, ThingSheetDto.class);
        } catch (IOException e) {
            throw new RuntimeException("error processing the Excel file: " + e.getMessage(), e);
        } finally {
            deleteFile(file);
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(
                requireNonNull(multipartFile.getOriginalFilename())
        );

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }

    private void deleteFile(File file) {
        if (file != null) {
            if (file.delete()) {
                System.out.println("file deleted successfully!");
            } else {
                System.out.println("couldn't delete the file!");
            }
        }
    }
}
