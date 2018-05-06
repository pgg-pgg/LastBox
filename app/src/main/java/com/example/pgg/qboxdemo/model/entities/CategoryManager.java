package com.example.pgg.qboxdemo.model.entities;

import android.content.Context;
import android.content.res.Resources;

import com.example.pgg.qboxdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pgg on 2018/5/5.
 */

public class CategoryManager {

    Context context;

    public CategoryManager(Context context){
        this.context=context;
    }

    public List<CategoryEntity> getAllCategory(){
        List<CategoryEntity> categoryEntityList =new ArrayList<>();
        Resources resources=context.getResources();
        String[] nameArray = resources.getStringArray(R.array.category_name);
        String[] typeArray = resources.getStringArray(R.array.category_type);

        for (int i=0;i<(nameArray.length>typeArray.length?typeArray.length:nameArray.length);i++){
            CategoryEntity categoryEntity = new CategoryEntity(null, nameArray[i], typeArray[i], i);
            categoryEntityList.add(categoryEntity);
        }
        return categoryEntityList;
    }
}
