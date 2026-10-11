package ph.edu.dlsu.lbycpds;

public class MultipleChoice {
    private String prompt;
    private String opt1;
    private String opt2;
    private String opt3;
    private String opt4;
    private String key;

    public MultipleChoice(String promptText, String opt1, String opt2, String opt3, String opt4, String key) {
        this.prompt = promptText;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.key = key.trim().toUpperCase();
    }

    public String getPrompt() { return prompt; }
    public String getOpt1() { return opt1; }
    public String getOpt2() { return opt2; }
    public String getOpt3() { return opt3; }
    public String getOpt4() { return opt4; }
    public String getKey() { return key; }
}