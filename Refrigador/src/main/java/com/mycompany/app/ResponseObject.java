package mycompany.app;

//import com.google.gson.annotations.SerialzedName;
import java.util.Arrays;
import java.util.List;

public class ResponseObject extends App {

    public class MealDBResult {
        MealDBMeal[] meals;
    }

    public class MealDBMeal {
        String idMeal;
        String strMeal;
        String strCategory;
        String strArea;
        String strInstructions;
        String strMealThumb;
        String strTags;
        String strYoutube;
        String strIngredient1, strIngredient2, strIngredient3, strIngredient4;
        String strIngredient5, strIngredient6, strIngredient7, strIngredient8;
        String strIngredient9, strIngredient10, strIngredient11, strIngredient12;
        String strIngredient13, strIngredient14, strIngredient15;
        String strSource;
        String strImageSource;
        String strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5;
        String strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10;
        String strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15;
        String idIngredient;
        String strIngredient;
        String strDescription;
    }

    List<MealDBMeal> mealList;
    List<String> ingredientList;

    public ResponseObject() {
        System.out.println("Currently not implemented");
            //this.mealList = Arrays.asList(ResponseObject.MealDBResult.meals);
        //ingredientList.addAll(strIngredient1,
        //                      strIngredient2,
        //                      strIngredient3,
        //                     strIngredient4,
        //                     strIngredient5,
        //                      strIngredient6,
        //                      strIngredient7,
        //                      strIngredient8,
        //                      strIngredient9,
        //                      strIngredient10,
        //                      strIngredient11,
        //                      strIngredient12,
        //                      strIngredient13,
        //                      strIngredient14,
        //                      strIngredient15);

    }
}
