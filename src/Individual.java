
public class Individual {

    private static final int ROW_LENGTH = 7;

    private Double[][]confusionMatrix;
    private Emotion[] emotionArray = new Emotion[ROW_LENGTH];

    private double contempt_fitnessValue, anger_fitnessValue, disgust_fitnessValue, fear_fitnessValue, happy_fitnessValue, sadness_fitnessValue, surprise_fitnessValue;;

    public Individual() {
        for (int i = 0; i < emotionArray.length; i++) {
            emotionArray[i] = new Emotion();
        }
    }

    public Individual(String[] values)
    {
        for (int i = 0; i < emotionArray.length; i++) {
            emotionArray[i] = new Emotion(values[i]);
        }
    }


    public double getContempt_fitnessValue() { return contempt_fitnessValue; }
    public double getAnger_fitnessValue() { return anger_fitnessValue; }
    public double getDisgust_fitnessValue() { return disgust_fitnessValue; }
    public double getFear_fitnessValue() { return fear_fitnessValue; }
    public double getHappy_fitnessValue() { return happy_fitnessValue; }
    public double getSadness_fitnessValue() { return sadness_fitnessValue; }
    public double getSurprise_fitnessValue() { return surprise_fitnessValue; }
    public Emotion[] getEmotionArray() { return emotionArray; }

    public void setEmotionArray(Emotion[] emotionArray) { this.emotionArray = emotionArray; }
    public void setContempt_fitnessValue(double neutral_fitnessValue) { this.contempt_fitnessValue = neutral_fitnessValue; }
    public void setAnger_fitnessValue(double anger_fitnessValue) { this.anger_fitnessValue = anger_fitnessValue; }
    public void setDisgust_fitnessValue(double disgust_fitnessValue) { this.disgust_fitnessValue = disgust_fitnessValue; }
    public void setFear_fitnessValue(double fear_fitnessValue) { this.fear_fitnessValue = fear_fitnessValue; }
    public void setHappy_fitnessValue(double happy_fitnessValue) { this.happy_fitnessValue = happy_fitnessValue; }
    public void setSadness_fitnessValue(double sadness_fitnessValue) { this.sadness_fitnessValue = sadness_fitnessValue; }
    public void setSurprise_fitnessValue(double suprise_fitnessValue) { this.surprise_fitnessValue = suprise_fitnessValue; }

    void printEmotionArray()
    {
        for (int i = 0; i < emotionArray.length; i++) {
            System.out.println(emotionArray[i].getSingleEmotion());
        }
        System.out.println();
    }
    public String toString(){
        StringBuilder stringBuilder=new StringBuilder();
        for (int i=0 ; i <emotionArray.length; i++ ){
            stringBuilder.append(emotionArray[i].getSingleEmotion());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    void printOnes()
    {
        for (int i = 0; i < emotionArray.length; i++) {
            int[] ones = emotionArray[i].getOnes();
            for (int j = 0; j < ones.length; j++) {
                System.out.print(ones[j] + ", ");
            }
            System.out.println();
        }
    }

    String[] slice(int firstIndex, int lastIndex)
    {
        String[] sliced = new String[ROW_LENGTH];
        for (int i = 0; i < ROW_LENGTH; i++) {
            sliced[i] = emotionArray[i].getSingleEmotion().substring(firstIndex, lastIndex);
        }
        return sliced;
    }

    String[] mutate(int firstIndex, int lastIndex)
    {
        String[] mutated = new String[ROW_LENGTH];
        for (int i = 0; i < ROW_LENGTH; i++) {
            mutated[i] = emotionArray[i].getSingleEmotion().substring(firstIndex, lastIndex);
        }
        return mutated;
    }

    void setNewContent(Individual p)
    {
        this.emotionArray = p.getEmotionArray();
        this.contempt_fitnessValue = p.getContempt_fitnessValue();
        this.anger_fitnessValue = p.getAnger_fitnessValue();
        this.disgust_fitnessValue = p.getDisgust_fitnessValue();
        this.fear_fitnessValue = p.getFear_fitnessValue();
        this.happy_fitnessValue = p.getHappy_fitnessValue();
        this.sadness_fitnessValue = p.getSadness_fitnessValue();
        this.surprise_fitnessValue = p.getSurprise_fitnessValue();
    }

    public Double[][] getConfusionMatrix() {
        return confusionMatrix;
    }

    public void setConfusionMatrix(Double[][] confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }
}


