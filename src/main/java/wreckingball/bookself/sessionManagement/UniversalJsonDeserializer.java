package wreckingball.bookself.sessionManagement;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import datamodel.Authors;
import datamodel.Books;

/**
 * Created by Cristian on 5/12/2015.
 */
public class UniversalJsonDeserializer
{
    public static class AuthorsDeserializer implements JsonDeserializer<Authors>
    {

        @Override
        public Authors deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Authors author = new Authors();

            author.setAuthorId(Integer.parseInt(jsonElement.getAsJsonObject().get("AuthorId").toString()));
            author.setAuthorFName(jsonElement.getAsJsonObject().get("firstName").toString());
            author.setAuthorLName(jsonElement.getAsJsonObject().get("lastName").toString());

            return author;
        }
    }

    public static class BookDeserializer implements JsonDeserializer<Books>
    {


       // @Override
        public Books deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
        {

            final JsonObject json = jsonElement.getAsJsonObject();
            final JsonElement JsonTitle = json.get("Title");
            final String title = JsonTitle.getAsString();

            final String isbn10 = json.get("ISBN10").getAsString();
            final String isbn13 = json.get("ISBN13").getAsString();




            final Books book = new Books();

            TypeToken<List<Authors>> type1 = new TypeToken<List<Authors>>() {
            };
           // List<Authors> authors = jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("Author"), type1.getType());

            book.setTitle(title);
            book.setISBN10(isbn10);
            book.setISBN13(isbn13);
           // book.setAuthors(authors);


           return book;

        }
    }



}
