package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

public class CardTemplate {
    private String templateFront;
    private String templateBack;
    private String styling;

    public String getTemplateFront() {
        return templateFront;
    }
    public void setTemplateFront(String templateFront) {
        this.templateFront = templateFront;
    }
    public String getTemplateBack() {
        return templateBack;
    }
    public void setTemplateBack(String templateBack) {
        this.templateBack = templateBack;
    }
    public String getStyling() {
        return styling;
    }
    public void setStyling(String styling) {
        this.styling = styling;
    }

    public CardTemplate() {
        templateFront = "{{front}}";
        templateBack = "{{back}}";
        styling = "";
    }
    public CardTemplate(String templateFront, String templateBack, String styling) {
        this.templateFront = templateFront;
        this.templateBack = templateBack;
        this.styling = styling;
    }

    Card render(ArrayList<String> fieldList, ArrayList<String> valueList) {
        Card card = new Card();
        card.htmlFront = CommonParser.render(templateFront, fieldList, valueList);
        card.htmlBack = CommonParser.render(templateBack, fieldList, valueList);
        card.css = CommonParser.render(styling, fieldList, valueList);
        return card;
    }

    static CardTemplate parse(JsonReader reader) throws IOException {
        CardTemplate tpl = new CardTemplate();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "templateFront":
                    tpl.templateFront = reader.nextString();
                    break;
                case "templateBack":
                    tpl.templateBack = reader.nextString();
                    break;
                case "styling":
                    tpl.styling = reader.nextString();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return tpl;
    }

    static ArrayList<CardTemplate> parseList(JsonReader reader) throws IOException {
        ArrayList<CardTemplate> ans = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ans.add(parse(reader));
        }
        reader.endArray();

        return ans;
    }
}
