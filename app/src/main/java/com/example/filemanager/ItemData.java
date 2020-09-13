package com.example.filemanager;

import java.io.File;


    public class ItemData {

        private String name;
        private String path;
        private boolean isDirectory;


        private int imgRes;


        public ItemData(String name, String path,int imgRes, boolean isDirectory) {

            this.name = name;
            this.path=path;
            this.imgRes=imgRes;
            this.isDirectory = isDirectory;


        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isDirectory() {
            return isDirectory;
        }

        public void setDirectory(boolean directory) {
            isDirectory = directory;
        }

        public int getImgRes() {
            return imgRes;
        }

        public void setImgRes(int imgRes) {
            this.imgRes = imgRes;
        }
    }


