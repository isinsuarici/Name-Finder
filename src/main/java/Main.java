import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class Main {
    public static void main(String[] args) throws IOException {

        String url=args.length>0?args[0]:"https://opennlp.apache.org/books-tutorials-and-talks.html";

        Document doc = Jsoup.connect(url).get();

        String str= String.valueOf(doc);

//*************Sentence detector model****************

        //Loading sentence detector model
        InputStream inputStream = new FileInputStream("src\\main\\resources\\en-sent.bin");
        SentenceModel model = new SentenceModel(inputStream);

        SentenceDetectorME detector = new SentenceDetectorME(model);

        //Detecting the sentence
        String[] sentences = detector.sentDetect(str);

        StringBuilder st= new StringBuilder();
        for (String sentence : sentences) {
            st.append(sentence);
        }
        //*************Tokenizer model************************

        //Loading the Tokenizer model
        InputStream inputStream2 = new FileInputStream("src\\main\\resources\\en-token.bin");
        TokenizerModel tokenModel = new TokenizerModel(inputStream2);

        TokenizerME tokenizer = new TokenizerME(tokenModel);

        //Tokenizing the given text
        String[] tokens = tokenizer.tokenize(st.toString());

//*************Name finder model***********************

        //Loading the Name Finder model
        InputStream inputStreamNameFinder = new FileInputStream("src\\main\\resources\\en-ner-person.bin");
        TokenNameFinderModel model2 = new TokenNameFinderModel(inputStreamNameFinder);

        NameFinderME nameFinder = new NameFinderME(model2);

        //Finding the names in the sentence
        Span[] nameSpans = nameFinder.find(tokens);

        //Printing the names and surnames in a sentence
        for(Span s: nameSpans)
            System.out.println(tokens[s.getStart()]+ " " + tokens[s.getStart()+1]);

        nameFinder.clearAdaptiveData();
    }
}