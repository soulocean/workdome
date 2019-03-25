package com.example.soulocean1.workdome;


import android.graphics.Color;

public class ImageInfor {


    public int cID;
    public String name;
    public String time;
    public String itemtype;
    public int imageId;
    public String isP;


    public ImageInfor(int cID, String name, String time,String itemtype,int imageId,String isP) {

        this.cID=cID;
        this.name = name;
        this.time = time;
        this.itemtype = itemtype;
        this.isP=isP;


        switch (imageId)
        {
            case 1:this.imageId = Color.parseColor("#CD9B9B") ;break;
            case 2:this.imageId = Color.parseColor("#CD919E") ;break;
            case 3:this.imageId = Color.parseColor("#CD8162") ;break;
            case 4:this.imageId = Color.parseColor("#CD6839") ;break;
            case 5:this.imageId = Color.parseColor("#CD5C5C") ;break;
            case 6:this.imageId = Color.parseColor("#8B4513") ;break;
            case 7:this.imageId = Color.parseColor("#8B008B") ;break;

        }

    }


    public int getcID() {
        return cID;
    }

    public void setcID(int cID) { this.cID = cID; }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getTime() { return time; }

    public void setTime(String time) {
        this.time = time;
    }



    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }


    public int getImageId() {
        return imageId;
    }

    public int get_INT_ImageId() {

            if( this.imageId == Color.parseColor("#CD9B9B") )imageId= 1;
            if( this.imageId == Color.parseColor("#CD919E") )imageId= 2;
            if( this.imageId == Color.parseColor("#CD8162") )imageId= 3;
            if( this.imageId == Color.parseColor("#CD6839") )imageId= 4;
            if( this.imageId == Color.parseColor("#CD5C5C") )imageId= 5;
            if( this.imageId == Color.parseColor("#8B4513") )imageId= 6;
            if( this.imageId == Color.parseColor("#8B008B") )imageId= 7;

            return imageId;
    }

    public void setImageId(int imageId) {

        switch (imageId)
        {
            case 1:this.imageId = Color.parseColor("#CD9B9B") ;break;
            case 2:this.imageId = Color.parseColor("#CD919E") ;break;
            case 3:this.imageId = Color.parseColor("#CD8162") ;break;
            case 4:this.imageId = Color.parseColor("#CD6839") ;break;
            case 5:this.imageId = Color.parseColor("#CD5C5C") ;break;
            case 6:this.imageId = Color.parseColor("#8B4513") ;break;
            case 7:this.imageId = Color.parseColor("#8B008B") ;break;

        }
    
    }

    public String getisP() { return isP; }

    public void setisP(String isP) {
        this.isP = isP;
    }






}
