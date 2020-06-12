import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EmotionRepresentation {

    private static final int INDIVIDUAL_COUNT = 200;
    private static final int EMOTION_COUNT = 6;
    private static final int FACS_COUNT = 95;
    private static final int PARENT_LENGTH = 4;
    private static final int CHILD_LENGTH = 2;
    private static int populationSize, tournamentSize;
    private static double crossoverThreshold, mutationThreshold;
    static Individual worst;
    static Individual best;

    static Individual[] individual = new Individual[INDIVIDUAL_COUNT];
    static Individual[] parent = new Individual[PARENT_LENGTH];
    static Individual[] child = new Individual[CHILD_LENGTH];
    static Double[]emotionsCounts={0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    static double[] bestFitnessValues={0,0,0,0,0,0,0};
    static ArrayList<ActionUnit> dbRows = new ArrayList<>();

    public EmotionRepresentation() {
        CleanEmotionFiles();
        int counter=0;
        double mutationPossibility, crossoverPossibility;


        readInput();
        constructDB();             //Dataseti arraye atıyor.
        calculateEmotionsCount(); //Her emotiondan datasette kaç tane var ona bakıyor.
        createIndividuals();

        for(int i = 0 ; i < populationSize ; i++)
        {
            System.out.println("POPULATION: " + i);
            match(individual[i]);  //Print Individuals with Fitness Values
        }

        while(counter < tournamentSize) {


            selectParents();            //Select 4 random number and choose first 2 biggest numbers as parents
            if(counter == 0){
                worst = individual[0];
            }
            else{
                worst=findWorstFitness(individual);               //Find the worst of population
            }


            Random rnd = new Random();
            crossoverPossibility = 100*rnd.nextDouble();
            //System.out.println(crossoverPossibility);
            if (crossoverPossibility <= crossoverThreshold) {
                crossover();            //Crossover possibility is calculated randomly
                for (int i=0; i<6;i++) {
                    mutationPossibility = 100*rnd.nextDouble();
                    if (mutationPossibility <= mutationThreshold) {
                        mutation(i);         //Mutation possibility is calculated  randomly
                    }
                }
            }  //System.out.println("No Crossover");

            if(counter%100==0)
                System.out.println();
            elitism();              //If fitness value of children is bigger than worst than elitism

            best=findBestFitness(individual);
            writeBestEmotionsToFile(best);
            counter++;
        }
        writeBestIndividualToFile();
    }

    static void calculateEmotionsCount(){

        for (ActionUnit actionUnit:dbRows) {
            switch (actionUnit.getEmotion()) {
                case "CONTEMPT":
                    emotionsCounts[0]++;
                    break;
                case "ANGER":
                    emotionsCounts[1]++;
                    break;
                case "DISGUST":
                    emotionsCounts[2]++;
                    break;
                case "FEAR":
                    emotionsCounts[3]++;
                    break;
                case "HAPPY":
                    emotionsCounts[4]++;
                    break;
                case "SADNESS":
                    emotionsCounts[5]++;
                    break;
                case "SURPRISE":
                    emotionsCounts[6]++;
                    break;
            }
        }
    }

    static void createIndividuals() {

        String[] array = {
                "00000000000001000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                "00010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                "00010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                "10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                "00000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                "10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                "11000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"};

        individual[0] = new Individual(array);

        for (int i = 1; i < populationSize; i++) {
            individual[i] = new Individual();          //Create 10 individuals
        }
    }

    static void selectParents(){
        // make a list of individuals
        Individual[] shuffledIndividual = new Individual[INDIVIDUAL_COUNT];

        for (int i = 0; i < individual.length; i++)
        {
            shuffledIndividual[i] = individual[i];
        }

        for (int i = 0; i < PARENT_LENGTH; i++)
        {
            parent[i] = shuffledIndividual[i];
        }

        //Her emotion için ayrı ayrı satırları değiştiriyoruz. Böylece karışık bir prant oluşmuş oluyor.
        for (int i = 0; i < EMOTION_COUNT; i++){
            shuffleAndSortIndividual(shuffledIndividual, i);
            for ( int n = 0; n < PARENT_LENGTH; n++) {
                parent[n].getEmotionArray()[i].setSingleEmotion(shuffledIndividual[n].getEmotionArray()[i].getSingleEmotion());
            }
        }

        System.out.println("Parent 1:");
        parent[0].printEmotionArray();
        System.out.println();
        System.out.println("Parent 2:");
        parent[1].printEmotionArray();
    }

    static void shuffleAndSortIndividual(Individual[] shuffledIndividual, int emotionValue){

        Random rnd = ThreadLocalRandom.current();

        /*Individual array'ini shuffle ediyor. İf caselerde her emotion için ayrı sort yapılıyor.
         Bu yüzden method selectParents tarafından 7 kere çağırılıyor. */
        for (int i = shuffledIndividual.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            Individual temp = shuffledIndividual[index];
            shuffledIndividual[index] = shuffledIndividual[i];
            shuffledIndividual[i] = temp;
        }

        //Contempt
        if(emotionValue == 0){
            for (int i = 0; i < individual.length; i++)
            {
                for (int j = 0; j < individual.length; j++)
                {
                    if (shuffledIndividual[i].getContempt_fitnessValue() > shuffledIndividual[j].getContempt_fitnessValue())
                    {
                        Individual temp = shuffledIndividual[j];
                        shuffledIndividual[j] = shuffledIndividual[i];
                        shuffledIndividual[i] = temp;
                    }
                }
            }
        }
        //Anger
        else if(emotionValue == 1){
            for (int i = 0; i < individual.length; i++)
            {
                for (int j = 0; j < individual.length; j++)
                {
                    if (shuffledIndividual[i].getAnger_fitnessValue() > shuffledIndividual[j].getAnger_fitnessValue())
                    {
                        Individual temp = shuffledIndividual[j];
                        shuffledIndividual[j] = shuffledIndividual[i];
                        shuffledIndividual[i] = temp;
                    }
                }
            }
        }
        //Disgust
        else if (emotionValue == 2){
            for (int i = 0; i < individual.length; i++)
            {
                for (int j = 0; j < individual.length; j++)
                {
                    if (shuffledIndividual[i].getDisgust_fitnessValue() > shuffledIndividual[j].getDisgust_fitnessValue())
                    {
                        Individual temp = shuffledIndividual[j];
                        shuffledIndividual[j] = shuffledIndividual[i];
                        shuffledIndividual[i] = temp;
                    }
                }
            }
        }
        //Fear
        else if(emotionValue == 3){
            for (int i = 0; i < individual.length; i++)
            {
                for (int j = 0; j < individual.length; j++)
                {
                    if (shuffledIndividual[i].getFear_fitnessValue() > shuffledIndividual[j].getFear_fitnessValue())
                    {
                        Individual temp = shuffledIndividual[j];
                        shuffledIndividual[j] = shuffledIndividual[i];
                        shuffledIndividual[i] = temp;
                    }
                }
            }
        }
        //Happy
        else if(emotionValue == 4){
            for (int i = 0; i < individual.length; i++)
            {
                for (int j = 0; j < individual.length; j++)
                {
                    if (shuffledIndividual[i].getHappy_fitnessValue() > shuffledIndividual[j].getHappy_fitnessValue())
                    {
                        Individual temp = shuffledIndividual[j];
                        shuffledIndividual[j] = shuffledIndividual[i];
                        shuffledIndividual[i] = temp;
                    }
                }
            }
        }
        //Sadness
        else if(emotionValue == 5){
            for (int i = 0; i < individual.length; i++)
            {
                for (int j = 0; j < individual.length; j++)
                {
                    if (shuffledIndividual[i].getSadness_fitnessValue() > shuffledIndividual[j].getSadness_fitnessValue())
                    {
                        Individual temp = shuffledIndividual[j];
                        shuffledIndividual[j] = shuffledIndividual[i];
                        shuffledIndividual[i] = temp;
                    }
                }
            }
        }
        //Suprise
        else if(emotionValue == 6){
            for (int i = 0; i < individual.length; i++)
            {
                for (int j = 0; j < individual.length; j++)
                {
                    if (shuffledIndividual[i].getSurprise_fitnessValue() > shuffledIndividual[j].getSurprise_fitnessValue())
                    {
                        Individual temp = shuffledIndividual[j];
                        shuffledIndividual[j] = shuffledIndividual[i];
                        shuffledIndividual[i] = temp;
                    }
                }
            }
        }
    }

    static void mutation(int mutateAtRow) {
        Random rnd = new Random();

        int mutateAtColumn =  1 + rnd.nextInt(90);
        int mutateAtChild = rnd.nextInt(2);

        String[] pieceM = child[mutateAtChild].mutate(mutateAtColumn,mutateAtColumn+1);
        String[] piece1 = child[mutateAtChild].mutate(0,FACS_COUNT);
        String[] piece2 = child[mutateAtChild].mutate(0,mutateAtColumn);
        String[] piece3 = child[mutateAtChild].mutate((mutateAtColumn+1),FACS_COUNT);
        String[] piece4 = child[mutateAtChild].mutate(0,FACS_COUNT);

        if(pieceM[mutateAtRow].equals("0")){
            pieceM[mutateAtRow] = pieceM[mutateAtRow].replace("0","1");
        }
        else if(pieceM[mutateAtRow].equals("1")){
            pieceM[mutateAtRow] = pieceM[mutateAtRow].replace("1","0");
        }

        String[] mutatedChild = new String[7];

        for (int i = 0; i < mutateAtRow; i++) {
            mutatedChild[i] = piece1[i];
        }
        for (int i = mutateAtRow+1; i < 7 ; i++) {
            mutatedChild[i] = piece4[i];
        }
        mutatedChild[mutateAtRow] = piece2[mutateAtRow] + pieceM[mutateAtRow] + piece3[mutateAtRow];

        System.out.println("MUTATED CHILD");
        for (int i = 0; i < 7; i++) {
            System.out.println(mutatedChild[i]);
        }

        child[mutateAtChild] = new Individual(mutatedChild);

        if(mutateAtChild == 0){
            child[0].setNewContent(child[mutateAtChild]);
            System.out.println();
            match(child[0]);
            //System.out.println("Mutated Child Fitness Value: " + child[0].getFitnessValue());
            System.out.println("Mutated Child Fitness Value: " + child[0].getContempt_fitnessValue() + ' ' + child[0].getAnger_fitnessValue() + ' ' + child[0].getDisgust_fitnessValue() + ' ' +
                    child[0].getHappy_fitnessValue() + ' ' + child[0].getSurprise_fitnessValue() + ' ' + child[0].getFear_fitnessValue() + ' ' + child[0].getSadness_fitnessValue());

        }

        else if(mutateAtChild == 1){
            child[1].setNewContent(child[mutateAtChild]);
            System.out.println();
            match(child[1]);
            //System.out.println("Mutated Child Fitness Value: " + child[1].getFitnessValue());
            System.out.println("Mutated Child Fitness Value: " + child[1].getContempt_fitnessValue() + ' ' + child[1].getAnger_fitnessValue() + ' ' + child[1].getDisgust_fitnessValue() + ' ' +
                    child[1].getHappy_fitnessValue() + ' ' + child[1].getSurprise_fitnessValue() + ' ' + child[1].getFear_fitnessValue() + ' ' + child[1].getSadness_fitnessValue());

        }

    }

    static void crossover() {
        Random rnd = new Random();

        int sliceAt =  1 + rnd.nextInt(26);

        System.out.println();

        String[] piece1 = parent[0].slice(0 ,sliceAt);
        String[] piece2 = parent[0].slice(sliceAt, FACS_COUNT);

        String[] piece3 = parent[1].slice(0 ,sliceAt);
        String[] piece4 = parent[1].slice(sliceAt, FACS_COUNT);


        String[] child1 = new String[7];
        String[] child2 = new String[7];

        for (int i = 0; i < 7; i++)
        {
            child1[i] = piece1[i] + piece4[i];
            child2[i] = piece2[i] + piece3[i];
        }

        child[0] = new Individual(child1);
        child[1] = new Individual(child2);

        System.out.println("Child 1:");
        for (int i = 0; i < 7; i++)
        {
            System.out.println(child1[i]);
        }

        System.out.println();

        System.out.println("Child 2:");
        for (int i = 0; i < 7; i++)
        {
            System.out.println(child2[i]);
        }

        System.out.println();
        System.out.println("Child 1 Fitness:");
        match(child[0]);

        System.out.println();
        System.out.println("Child 2 Fitness:");
        match(child[1]);

        System.out.println("Child 1 Fitness : "  + child[0].getContempt_fitnessValue() + ' ' + child[0].getAnger_fitnessValue() + ' ' + child[0].getDisgust_fitnessValue() + ' ' +
                child[0].getHappy_fitnessValue() + ' ' + child[0].getSurprise_fitnessValue() + ' ' + child[0].getFear_fitnessValue() + ' ' + child[0].getSadness_fitnessValue());

        System.out.println("Child 2 Fitness : " + child[1].getContempt_fitnessValue() + ' ' + child[1].getAnger_fitnessValue() + ' ' + child[1].getDisgust_fitnessValue() + ' ' +
                child[1].getHappy_fitnessValue() + ' ' + child[1].getSurprise_fitnessValue() + ' ' + child[1].getFear_fitnessValue() + ' ' + child[1].getSadness_fitnessValue());

    }

    static void elitism() {
        best = individual[0];

        double child0_fitnessValue = child[0].getFear_fitnessValue() + child[0].getSurprise_fitnessValue() + child[0].getSadness_fitnessValue()
                + child[0].getHappy_fitnessValue() + child[0].getDisgust_fitnessValue() + child[0].getAnger_fitnessValue() + child[0].getContempt_fitnessValue();

        double worst_fitnessValue = worst.getFear_fitnessValue() + worst.getSurprise_fitnessValue() + worst.getSadness_fitnessValue()
                + worst.getHappy_fitnessValue() + worst.getDisgust_fitnessValue() + worst.getAnger_fitnessValue() + worst.getContempt_fitnessValue();

        double child1_fitnessValue = child[1].getFear_fitnessValue() + child[1].getSurprise_fitnessValue() + child[1].getSadness_fitnessValue()
                + child[1].getHappy_fitnessValue() + child[1].getDisgust_fitnessValue() + child[1].getAnger_fitnessValue() + child[1].getContempt_fitnessValue();

        if(child0_fitnessValue >= worst_fitnessValue){
            worst.setNewContent(child[0]);
        }
        worst=findWorstFitness(individual);

        if(child1_fitnessValue >= worst_fitnessValue){
            worst.setNewContent(child[1]);
        }
        System.out.println();
    }

    static void match(Individual p) {

        String[] emotion = {"CONTEMPT","ANGER", "DISGUST", "FEAR", "HAPPY", "SADNESS", "SURPRISE"};
        Double[]matchedEmotions={0.0,0.0,0.0,0.0,0.0,0.0,0.0};
        Double [] notMatchedS={0.0,0.0,0.0,0.0,0.0,0.0,0.0};
        Double[][]confusionMatrix={{0.0,0.0,0.0,0.0,0.0,0.0,0.0},
                {0.0,0.0,0.0,0.0,0.0,0.0,0.0},
                {0.0,0.0,0.0,0.0,0.0,0.0,0.0},
                {0.0,0.0,0.0,0.0,0.0,0.0,0.0},
                {0.0,0.0,0.0,0.0,0.0,0.0,0.0},
                {0.0,0.0,0.0,0.0,0.0,0.0,0.0},
                {0.0,0.0,0.0,0.0,0.0,0.0,0.0}};
        int notMatchedCount=0;
        for (int i = 0; i < p.getEmotionArray().length; i++)
        {
            int[] ones = p.getEmotionArray()[i].getOnes();

            ArrayList<ActionUnit> arrayList = findDbLines(ones);

            for (ActionUnit e : arrayList)
            {
                //System.out.println("Match found at database line: " + e.getFirstCol() + " " + e.getSecondCol() + " with binary array line " + (i + 1) + ", Emotion: " + emotion[i]);
                if (e.getEmotion().contentEquals(emotion[i]))
                {
                    confusionMatrix[i][i]++;
                    matchedEmotions[i]++;
                }
                else{
                    notMatchedS[i]++;
                    if(e.getEmotion().contentEquals(emotion[0])){
                        confusionMatrix[0][i]++;
                    }
                    else if(e.getEmotion().contentEquals(emotion[1])){
                        confusionMatrix[1][i]++;
                    }
                    else if(e.getEmotion().contentEquals(emotion[2])){
                        confusionMatrix[2][i]++;
                    }
                    else if(e.getEmotion().contentEquals(emotion[3])){
                        confusionMatrix[3][i]++;
                    }
                    else if(e.getEmotion().contentEquals(emotion[4])){
                        confusionMatrix[4][i]++;
                    }
                    else if(e.getEmotion().contentEquals(emotion[5])){
                        confusionMatrix[5][i]++;
                    }
                    else if(e.getEmotion().contentEquals(emotion[6])){
                        confusionMatrix[6][i]++;
                    }
                    notMatchedCount++;
                }
            }
        }
        for (int i=0;i<7;i++){
            confusionMatrix[i]=divideArraybyVal(confusionMatrix[i],emotionsCounts[i]);
        }
        p.setConfusionMatrix(confusionMatrix);
        p.setContempt_fitnessValue(matchedEmotions[0]/emotionsCounts[0]);
        p.setAnger_fitnessValue(matchedEmotions[1]/emotionsCounts[1]);
        p.setDisgust_fitnessValue(matchedEmotions[2]/emotionsCounts[2]);
        p.setFear_fitnessValue(matchedEmotions[3]/emotionsCounts[3]);
        p.setHappy_fitnessValue(matchedEmotions[4]/emotionsCounts[4]);
        p.setSadness_fitnessValue(matchedEmotions[5]/emotionsCounts[5]);
        p.setSurprise_fitnessValue(matchedEmotions[6]/emotionsCounts[6]);

        System.out.println("Fitness Value of Contempt: " + p.getContempt_fitnessValue());
        System.out.println("Fitness Value of Anger: " + p.getAnger_fitnessValue());
        System.out.println("Fitness Value of Disgust: " + p.getDisgust_fitnessValue());
        System.out.println("Fitness Value of Fear: " + p.getFear_fitnessValue());
        System.out.println("Fitness Value of Happy: " + p.getHappy_fitnessValue());
        System.out.println("Fitness Value of Sadness: " + p.getSadness_fitnessValue());
        System.out.println("Fitness Value of Suprise: " + p.getSurprise_fitnessValue());
    }

    static Individual findBestFitness(Individual[] individual){
        double individual_fitnessValue;

        Individual bestIndividual = individual[0];
        double bestFitnessValue = individual[0].getFear_fitnessValue() + individual[0].getSurprise_fitnessValue() + individual[0].getSadness_fitnessValue()
                + individual[0].getHappy_fitnessValue() + individual[0].getDisgust_fitnessValue() + individual[0].getAnger_fitnessValue() + individual[0].getContempt_fitnessValue();

        for (Individual value : individual) {
            individual_fitnessValue = value.getFear_fitnessValue() + value.getSurprise_fitnessValue() + value.getSadness_fitnessValue()
                    + value.getHappy_fitnessValue() + value.getDisgust_fitnessValue() + value.getAnger_fitnessValue() + value.getContempt_fitnessValue();

            if (individual_fitnessValue > bestFitnessValue) {
                bestIndividual = value;
            }

        }

        System.out.println("BEST FITNESS: " + bestIndividual.getContempt_fitnessValue() + ' ' + bestIndividual.getAnger_fitnessValue() + ' ' + bestIndividual.getDisgust_fitnessValue() + ' ' +
                bestIndividual.getHappy_fitnessValue() + ' ' + bestIndividual.getSurprise_fitnessValue() + ' ' + bestIndividual.getFear_fitnessValue() + ' ' + bestIndividual.getSadness_fitnessValue());

        return bestIndividual;
    }

    static Individual findWorstFitness(Individual[] individual){
        double individual_fitnessValue;

        Individual worstIndividual = individual[0];
        double worstFitnessValue = worstIndividual.getFear_fitnessValue() +
                worstIndividual.getSurprise_fitnessValue() +
                worstIndividual.getSadness_fitnessValue()+
                worstIndividual.getHappy_fitnessValue() +
                worstIndividual.getDisgust_fitnessValue()+
                worstIndividual.getAnger_fitnessValue() +
                worstIndividual.getContempt_fitnessValue();

        for (Individual value : individual) {
            individual_fitnessValue = value.getFear_fitnessValue() + value.getSurprise_fitnessValue() + value.getSadness_fitnessValue()
                    + value.getHappy_fitnessValue() + value.getDisgust_fitnessValue() + value.getAnger_fitnessValue() + value.getContempt_fitnessValue();

            if (individual_fitnessValue < worstFitnessValue) {
                worstIndividual = value;
            }
        }
        return worstIndividual;
        //System.out.println("WORST FITNESS: " + worst.getFitnessValue());
    }

    static void readInput() {
        try {
            FileInputStream fstream = new FileInputStream("input.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str;
            while ((str = br.readLine()) != null) {
                String[] tokens = str.split(" ");
                populationSize = Integer.parseInt(tokens[0]);
                tournamentSize = Integer.parseInt(tokens[1]);
                crossoverThreshold = Double.parseDouble(tokens[2]);
                mutationThreshold = Double.parseDouble(tokens[3]);
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    static void constructDB() {
        try {
            FileInputStream fstream = new FileInputStream("test.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str;

            while ((str = br.readLine()) != null) {
                dbRows.add(new ActionUnit(str));
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    static ArrayList<ActionUnit> findDbLines(int[] ones) {
        ArrayList<ActionUnit> foundLines = new ArrayList<>();      //Create a arraylist with foundLines

        int onesCount;
        for (onesCount = 0; onesCount < ones.length; onesCount++) {
            if (ones[onesCount] == 0)
            {
                break;
            }
        }

        for (ActionUnit e : dbRows)
        {
            int foundCount = 0;
            int[] facs = e.getFacsCode();

            for (int i = 0; i < onesCount; i++)
            {
                for (int fac : facs) {
                    if (ones[i] == fac) {
                        foundCount++;
                        break;
                    }
                }
            }

            if (foundCount != 0 && onesCount == foundCount)
            {
                foundLines.add(e);
            }
        }
        return foundLines;
    }

    static void writeToFile(String path,String bff) {    //write output to file
        Writer writer;
        try
        {
            writer = new BufferedWriter(new FileWriter(path, true));     //write instructions line by line
            writer.write(bff );
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void CleanEmotionFiles(){
        CleanFile("Anger.txt");
        CleanFile("Disgust.txt");
        CleanFile("Fear.txt");
        CleanFile("Happy.txt");
        CleanFile("Contempt.txt");
        CleanFile("Sadness.txt");
        CleanFile("Surprise.txt");
    }

    static void CleanFile(String path) {    //write output to file
        Writer writer;
        try
        {
            writer = new BufferedWriter(new FileWriter(path, false));     //write instructions line by line
            writer.write("");
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void writeBestEmotionsToFile(Individual best){
        if(bestFitnessValues[0]<best.getContempt_fitnessValue())
            bestFitnessValues[0]=best.getContempt_fitnessValue();
        if(bestFitnessValues[1]<best.getAnger_fitnessValue())
            bestFitnessValues[1]=best.getAnger_fitnessValue();
        if(bestFitnessValues[2]<best.getDisgust_fitnessValue())
            bestFitnessValues[2]=best.getDisgust_fitnessValue();
        if(bestFitnessValues[3]<best.getFear_fitnessValue())
            bestFitnessValues[3]=best.getFear_fitnessValue();
        if(bestFitnessValues[4]<best.getHappy_fitnessValue())
            bestFitnessValues[4]=best.getHappy_fitnessValue();
        if(bestFitnessValues[5]<best.getSadness_fitnessValue())
            bestFitnessValues[5]=best.getSadness_fitnessValue();
        if(bestFitnessValues[6]<best.getSurprise_fitnessValue())
            bestFitnessValues[6]=best.getSurprise_fitnessValue();
        writeToFile("Contempt.txt",bestFitnessValues[0]+" \n");
        writeToFile("Anger.txt",bestFitnessValues[1]+" \n");
        writeToFile("Disgust.txt",bestFitnessValues[2]+" \n");
        writeToFile("Fear.txt",bestFitnessValues[3]+" \n");
        writeToFile("Happy.txt",bestFitnessValues[4]+" \n");
        writeToFile("Sadness.txt",bestFitnessValues[5]+" \n");
        writeToFile("Surprise.txt",bestFitnessValues[6]+" \n");
    }

    static void writeBestIndividualToFile(){
        NumberFormat formatter = new DecimalFormat("#0.00");
        String[] emotion = {"CONTEMPT","ANGER", "DISGUST", "FEAR", "HAPPY", "SADNESS", "SURPRISE"};
        writeToFile("best_Individual.txt",best.toString());
        writeToFile("best_Individual.txt","Contempt:"+bestFitnessValues[0]+"\n");
        writeToFile("best_Individual.txt","Anger:"+bestFitnessValues[1]+"\n");
        writeToFile("best_Individual.txt","Disgust:"+bestFitnessValues[2]+"\n");
        writeToFile("best_Individual.txt","Fear:"+bestFitnessValues[3]+"\n");
        writeToFile("best_Individual.txt","Happy:"+bestFitnessValues[4]+"\n");
        writeToFile("best_Individual.txt","Sadness:"+bestFitnessValues[5]+"\n");
        writeToFile("best_Individual.txt","Surprise:"+bestFitnessValues[6]+"\n");
        writeToFile("best_Individual.txt","           CONTEMPT  ANGER     DISGUST   FEAR      " +
                "HAPPY     SADNESS   SURPRISE");
        for (int i=0;i<7;i++){
            writeToFile("best_Individual.txt","\n"+String.format("%-10s",emotion[i])+":");
            for(int j=0;j<7;j++){
                writeToFile("best_Individual.txt",String.format("%-10s",formatter.format(best.getConfusionMatrix()[i][j])));

            }
        }
        writeToFile("best_Individual.txt","\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" +
                "+++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
    }

    static double sum(Double[]values) {
        double result = 0;
        for (Double value:values)
            result += value;
        return result;
    }

    static Double[] divideArraybyVal(Double[]doubles,Double val){
        Double[]newDouble = new Double[doubles.length];
        for (int i=0;i<doubles.length;i++){
            newDouble[i]=doubles[i]/val;
        }
        return newDouble;
    }
}