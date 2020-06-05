import java.util.Random;

public class Emotion {

    private static final int COLUMN_LENGTH = 95;

    private String singleEmotion;

    private int[] ones = new int[COLUMN_LENGTH];
    //Create String binary line and count ones

    public Emotion()
    {
        Random rnd = new Random();

        singleEmotion = "";
        int onesCount = 0;
        int twosCount = 0;

        for (int j = 1; j <COLUMN_LENGTH +1 ; j++) {

            int number_of_facs = rnd.nextInt(100);

            if (number_of_facs < 90){
                singleEmotion += "0";
            }
            else if(number_of_facs < 100) {
                singleEmotion += "1";
                ones[onesCount] = j;
                onesCount++;
            }
        }
    }

    public Emotion(String singleEmotion) {

        this.singleEmotion = singleEmotion;
        int onesIndex = 0;

        for (int i = 0; i < COLUMN_LENGTH - 1; i++) {
            if (singleEmotion.charAt(i) == '1')
            {
                ones[onesIndex] = i + 1;
                onesIndex++;
            }
        }
    }

    public String getSingleEmotion()
    {
        return singleEmotion;
    }

    public void setSingleEmotion(String singleEmotion) {
        this.singleEmotion = singleEmotion;
    }

    public int[] getOnes()
    {
        return ones;
    }


}
