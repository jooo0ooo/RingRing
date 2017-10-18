package mokpoharbor.ringring;

/**
 * Created by pingrae on 2017. 10. 18..
 */

public class Variables {

    // 학생은 1, 교수님은 2
    private static int identity = 0;


    public static void setIdentity(int identity){
        Variables.identity = identity;
    }

    public static int getIdentity(){
        return Variables.identity;
    }

}
