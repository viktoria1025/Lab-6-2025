package functions;
import java.io.*;

public class FunctionPoint implements Serializable {
    private double x;
    private double y;

    public FunctionPoint(double x, double y){ // создает обЬект точки с заданными координатами
        this.x=x;
        this.y=y;
    }
    public FunctionPoint(FunctionPoint point){ // создает обЬект точки с теми же координатами что у указанной точки
        this.x=point.x;
        this.y=point.y;
    }
    public FunctionPoint(){  // создает точку с координатами 0.0
        this(0.0,0.0);
    }
    public  double getX(){//получение доступа к значению поля x из другого класса
        return x;
    }
    public void setX(double x){
        this.x=x;
    }
    public  double getY(){//получение доступа к значению поля y из другого класса
        return y;
    }
    public void setY(double y){
        this.y=y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    public boolean equals(Object a) {
        if (this == a)
            return true;
        if (a == null || getClass() != a.getClass())
            return false;

        FunctionPoint that = (FunctionPoint) a;
        return Math.abs(this.x - that.x) < 1e-10 && Math.abs(this.y - that.y) < 1e-10;
    }
    public int hashCode() {
        long xBit = Double.doubleToLongBits(x);
        long yBit = Double.doubleToLongBits(y);

        int xHash = (int)(xBit ^ (xBit >>> 32));
        int yHash = (int)(yBit ^ (yBit >>> 32));

        return xHash ^ yHash;
    }
    public Object clone() {
        return new FunctionPoint(this);
    }


}

