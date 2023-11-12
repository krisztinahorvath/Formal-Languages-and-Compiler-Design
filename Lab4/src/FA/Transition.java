package FA;

public class Transition {
    private String from;
    private String to;
    private String element;

    public Transition(String _from, String _to, String _element){
        from = _from;
        to = _to;
        element = _element;
    }

    public String getFrom(){
        return from;
    }

    public String getTo(){
        return to;
    }

    public String getElement(){
        return element;
    }
}

