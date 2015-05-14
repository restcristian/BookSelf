package wreckingball.bookself;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Objects;

import datamodel.Authors;
import datamodel.BookLists;
import datamodel.Books;
import datamodel.Users;

/**
 * Created by Cristian on 4/22/2015.
 */
public class UniversalJsonSerializer {

    public static class UserSerializer  implements JsonSerializer<Users> {

        @Override
        public JsonElement serialize(final Users users, final Type type, final JsonSerializationContext jsonSerializationContext) {
            JsonObject result = new JsonObject();

            result.add("FirstName", new JsonPrimitive(users.getFirstName()));
            result.add("LastName", new JsonPrimitive(users.getLastName()));
            result.add("Email", new JsonPrimitive(users.getEmail()));
            result.add("PW", new JsonPrimitive(users.getPassword()));

            return result;
        }

    }

    public static class BookListSerializer implements JsonSerializer<BookLists>
    {

        @Override
        public JsonElement serialize(BookLists bookLists, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject result = new JsonObject();

            result.add("ListName", new JsonPrimitive(bookLists.getListName()));
            result.add("ListType", new JsonPrimitive(bookLists.getListType()));
            if (bookLists.getBooklist() != null) {
                for (Books book : bookLists.getBooklist())
                {
                     result.add("", new UniversalJsonSerializer.BookSerializer().serialize(book, Books.class, jsonSerializationContext));
                }
            }
            return result;
        }
    }
    public static class BookSerializer implements JsonSerializer<Books>
    {

        @Override
        public JsonElement serialize(Books books, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject result = new JsonObject();

            result.add("BookID",new JsonPrimitive(books.getBookID()));
            result.add("ISBN10",new JsonPrimitive(books.getISBN10()));
            result.add("ISBN13",new JsonPrimitive(books.getISBN13()));
            result.add("Title",new JsonPrimitive(books.getTitle()));
            for(Authors author: books.getAuthors())
            {
                result.add("AuthorId",new JsonPrimitive(author.getAuthorId()));
                result.add("AuthorFName",new JsonPrimitive(author.getAuthorFName()));
                result.add("AuthorLName", new JsonPrimitive(author.getAuthorLName()));
            }
            result.add("Description",new JsonPrimitive(books.getDescription()));
            result.add("Cover",new JsonPrimitive(books.getCover()));
            result.add("Category",new JsonPrimitive(books.getCategory()));

            return result;

        }


    }

    public static class UserNamePasswordSerializer implements JsonSerializer<Users> {

        @Override
        public JsonElement serialize(final Users users, final Type type, final JsonSerializationContext jsonSerializationContext) {
            JsonObject result = new JsonObject();

            result.add("Email", new JsonPrimitive(users.getEmail()));
            result.add("PW", new JsonPrimitive(users.getPassword()));

            return result;
        }
    }


    public static String parseToJson(Type type, Object jsonSerializer, Object obj)
    {
       Gson gson = new GsonBuilder().registerTypeAdapter(type.getClass(), jsonSerializer).create();
       //return gson.toJson(obj).replace(".", "!").replace(":", "$");
        return gson.toJson(obj);
    }

    public static String convertToJason(String...args)
    {
        String json="{";
        String c;
        for(int x = 0; x < args.length - 1; x++)
        {
            c =(x == args.length - 2 )?"":",";
            json+= "\""+args[x]+"\"" + ":" +"\""+args[x+1]+"\""+c;
        }
        json+="}";
        return json;
    }

}
