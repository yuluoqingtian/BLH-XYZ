package javabean;

public class ExcelItem {
    //private String key;
    private String X;
    private String Y;
    private String Z;
    private String B;
    private String L;
    private String H;

    public ExcelItem( String x, String y, String z, String b, String l, String h) {
        //this.key = key;
        X = x;
        Y = y;
        Z = z;
        B = b;
        L = l;
        H = h;
    }

//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }

    public String getZ() {
        return Z;
    }

    public void setZ(String z) {
        Z = z;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }

    public String getH() {
        return H;
    }

    public void setH(String h) {
        H = h;
    }

    public String toFullString() {
        return "ExcelItem{" +
                //"key='" + key + '\'' +
                ", X='" + X + '\'' +
                ", Y='" + Y + '\'' +
                ", Z='" + Z + '\'' +
                ", B='" + B + '\'' +
                ", L='" + L + '\'' +
                ", H='" + H + '\'' +
                '}';
    }


    @Override
    public String toString() {
       return X+" ?"+Y+" ?"+Z+" ?"+B+" ?"+L+" ?"+H+" ";
    }
}
