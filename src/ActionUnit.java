public class ActionUnit{

    private String subject;
    private String section;
    private int[] facs;
    private String emotion;


    public ActionUnit(String line)
    {
        createEmotionArray(line);
    }

    public String getSubject() {
        return subject;
    }

    public String getSection() {
        return section;
    }

    public int[] getFacsCode() {
        return facs;
    }

    public String getEmotion() {
        return emotion;
    }

    public void createEmotionArray(String line)
    {
        String[] column = line.split("\t");

        subject = column[0];
        section = column[1];
        emotion = column[3];

        String[] dataset = column[2].split("\\+");

        facs = new int[dataset.length];

        for (int i=0; i<dataset.length; i++)
        {
            switch (dataset[i]) {

                case "4a":
                    facs[i] = 3;
                    break;
                case "4d":
                    facs[i] = 8;
                    break;
                case "5a":
                    facs[i] = 13;
                    break;
                case "5b":
                    facs[i] = 19;
                    break;
                case "5d":
                    facs[i] = 22;
                    break;
                case "5e":
                    facs[i] = 29;
                    break;
                case "7a":
                    facs[i] = 30;
                    break;
                case "7b":
                    facs[i] = 32;
                    break;
                case "7c":
                    facs[i] = 33;
                    break;
                case "7d":
                    facs[i] = 35;
                    break;
                case "7e":
                    facs[i] = 36;
                    break;
                case "9d":
                    facs[i] = 37;
                    break;
                case "9e":
                    facs[i] = 40;
                    break;
                case "10b":
                    facs[i] = 41;
                    break;
                case "12a":
                    facs[i] = 42;
                    break;
                case "12b":
                    facs[i] = 44;
                    break;
                case "12c":
                    facs[i] = 45;
                    break;
                case "12d":
                    facs[i] = 46;
                    break;
                case "14b":
                    facs[i] = 47;
                    break;
                case "14c":
                    facs[i] = 48;
                    break;
                case "14d":
                    facs[i] = 49;
                    break;
                case "15b":
                    facs[i] = 50;
                    break;
                case "15c":
                    facs[i] = 51;
                    break;
                case "15d":
                    facs[i] = 52;
                    break;
                case "16c":
                    facs[i] = 53;
                    break;
                case "16d":
                    facs[i] = 54;
                    break;
                case "16e":
                    facs[i] = 55;
                    break;
                case "17a":
                    facs[i] = 56;
                    break;
                case "17b":
                    facs[i] = 57;
                    break;
                case "17c":
                    facs[i] = 58;
                    break;
                case "17d":
                    facs[i] = 59;
                    break;
                case "17e":
                    facs[i] = 60;
                    break;
                case "20a":
                    facs[i] = 61;
                    break;
                case "20b":
                    facs[i] = 62;
                    break;
                case "20c":
                    facs[i] = 63;
                    break;
                case "20d":
                    facs[i] = 64;
                    break;
                case "20e":
                    facs[i] = 65;
                    break;
                case "23b":
                    facs[i] = 66;
                    break;
                case "23c":
                    facs[i] = 67;
                    break;
                case "23d":
                    facs[i] = 68;
                    break;
                case "23e":
                    facs[i] = 69;
                    break;
                case "24a":
                    facs[i] = 70;
                    break;
                case "24b":
                    facs[i] = 71;
                    break;
                case "24c":
                    facs[i] = 72;
                    break;
                case "24d":
                    facs[i] = 73;
                    break;
                case "24e":
                    facs[i] = 74;
                    break;
                case "26a":
                    facs[i] = 75;
                    break;
                case "38d":
                    facs[i] = 76;
                    break;
                case "43b":
                    facs[i] = 77;
                    break;
                case "45a":
                    facs[i] = 78;
                    break;
                case "45b":
                    facs[i] = 79;
                    break;
                case "45d":
                    facs[i] = 80;
                    break;
                case "54b":
                    facs[i] = 81;
                    break;
                case "62c":
                    facs[i] = 82;
                    break;
                case "63a":
                    facs[i] = 83;
                    break;
                case "63b":
                    facs[i] = 84;
                    break;
                case "64a":
                    facs[i] = 85;
                    break;
                case "64b":
                    facs[i] = 86;
                    break;
                case "R6":
                    facs[i] = 87;
                    break;
                case "R7":
                    facs[i] = 88;
                    break;
                case "L6":
                    facs[i] = 89;
                    break;
                case "L10":
                    facs[i] = 90;
                    break;
                case "L14":
                    facs[i] = 91;
                    break;
                case "R2":
                    facs[i] = 92;
                    break;
                case "R4a":
                    facs[i] = 93;
                    break;
                case "T23":
                    facs[i] = 94;
                    break;
                case "R20":
                    facs[i] = 95;
                    break;
                default:
                    facs[i] = Integer.parseInt(dataset[i]);
                    break;
            } //end of switch-case
        } //end of for
    }

}
