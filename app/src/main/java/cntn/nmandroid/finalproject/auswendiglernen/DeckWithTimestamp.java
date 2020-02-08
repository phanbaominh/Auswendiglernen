package cntn.nmandroid.finalproject.auswendiglernen;

public class DeckWithTimestamp extends Deck {
    private String createAt;

    public DeckWithTimestamp(Deck deck) {
        this.id = deck.id;
        this.name = deck.name;
        this.noteList = deck.noteList;
        this.createAt = Helper.GetCurrentTime();
    }

    public DeckWithTimestamp() {

    }

    public String getCreateAt() {
        return createAt;
    }
    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
