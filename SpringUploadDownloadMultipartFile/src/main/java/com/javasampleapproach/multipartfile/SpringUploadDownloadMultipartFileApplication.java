package com.javasampleapproach.multipartfile;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.javasampleapproach.multipartfile.filestorage.FileStorage;

@SpringBootApplication
public class SpringUploadDownloadMultipartFileApplication implements CommandLineRunner {
	 
	@Resource
	FileStorage fileStorage;
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringUploadDownloadMultipartFileApplication.class, args);
    }
    
	@Override
	public void run(String... args) throws Exception {
		fileStorage.deleteAll();
		fileStorage.init();
	}
 
}