package com.potatoprogrammers.doit;

public class ImageService {
    private static ImageService imageService;
    //private Set<String, >

    public static ImageService getInstance() {
        if (imageService == null) {
            imageService = new ImageService();
        }
        return imageService;
    }


}
