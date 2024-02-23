package com.inventics.e_commerce.modal;

public class Product {

    String category,description,image,title;
    int id;

    Rating rating;



    Double price;

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setPrice(Double price) {
        this.price = price;
    }


    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }


    public Double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public Product(String category, String description, String image, String title, int id, Double price,Rating rating) {
        this.category = category;
        this.description = description;
        this.image = image;
        this.title = title;
        this.price = price;
        this.id = id;
        this.rating = rating;
    }

    public  Product(){

    }



    public static class Rating {
        int count;
        float rate;

        public Rating(int count, float rate) {
            this.count = count;
            this.rate = rate;
        }
        public Rating(){

        }

        public int getCount() {
            return count;
        }

        public float getRate() {
            return rate;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setRate(float rate) {
            this.rate = rate;
        }
    }

}
