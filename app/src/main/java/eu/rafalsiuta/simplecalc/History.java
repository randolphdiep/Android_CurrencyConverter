package eu.rafalsiuta.simplecalc;

public class History {

    private String codeInput, codeOutput, rate, input, output, time;

    public History(String codeInput, String codeOutput, String input, String output, String rate, String time) {
        this.codeInput = codeInput;
        this.codeOutput = codeOutput;
        this.rate = rate;
        this.input = input;
        this.output = output;
        this.time = time;
    }

    public String getCodeInput() {
        return codeInput;
    }

    public void setCodeInput(String codeInput) {
        this.codeInput = codeInput;
    }

    public String getCodeOutput() {
        return codeOutput;
    }

    public void setCodeOutput(String codeOutput) {
        this.codeOutput = codeOutput;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}

