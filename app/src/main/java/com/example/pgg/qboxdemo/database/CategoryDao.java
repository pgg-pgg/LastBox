package com.example.pgg.qboxdemo.database;

import android.content.Context;

import com.example.pgg.qboxdemo.greendao.db.CategoryEntityDao;
import com.example.pgg.qboxdemo.greendao.db.DaoMaster;
import com.example.pgg.qboxdemo.greendao.db.DaoSession;
import com.example.pgg.qboxdemo.model.entities.CategoryEntity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by pgg on 2018/5/5.
 *
 * 种类标签存储到数据库的dao
 */

public class CategoryDao {

    public DBManager mDbManager;
    public CategoryDao(Context context){
        mDbManager=DBManager.getInstance(context);
    }

    /**
     * 插入一条记录
     * @param categoryEntity
     */
    public void insertCategory(CategoryEntity categoryEntity){
        DaoMaster daoMaster=new DaoMaster(mDbManager.getWriteableDatabase());
        DaoSession daoSession=daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = daoSession.getCategoryEntityDao();
        categoryEntityDao.insert(categoryEntity);
    }

    /**
     * 插入一列数据
     */
    public void insertCategoryList(List<CategoryEntity> categoryEntities){
        if (categoryEntities==null||categoryEntities.isEmpty()){
            return;
        }
        DaoMaster daoMaster = new DaoMaster(mDbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = daoSession.getCategoryEntityDao();
        categoryEntityDao.insertOrReplaceInTx(categoryEntities);
    }

    /**
     * 删除一条数据
     */
    public void deleteCategory(CategoryEntity categoryEntity){
        DaoMaster daoMaster = new DaoMaster(mDbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = daoSession.getCategoryEntityDao();
        categoryEntityDao.delete(categoryEntity);
    }

    /**
     * 删除所有记录
     */
    public void deleteAllCategory(){
        DaoMaster daoMaster = new DaoMaster(mDbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = daoSession.getCategoryEntityDao();
        categoryEntityDao.deleteAll();
    }

    /**
     * 更新数据
     * @param categoryEntity
     */
    public void updateCategory(CategoryEntity categoryEntity){
        DaoMaster daoMaster = new DaoMaster(mDbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = daoSession.getCategoryEntityDao();
        categoryEntityDao.update(categoryEntity);
    }

    public List<CategoryEntity> queryCategoryList(){
        DaoMaster daoMaster = new DaoMaster(mDbManager.getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = daoSession.getCategoryEntityDao();
        QueryBuilder<CategoryEntity> qb = categoryEntityDao.queryBuilder();
        List<CategoryEntity> list = qb.orderAsc(CategoryEntityDao.Properties.Order).list();
        return list;
    }
}
