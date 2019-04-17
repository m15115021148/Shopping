package com.geek.shopping.database;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * tips:
 *   Realm have some adapter,RealmBaseAdapter,RealmRecyclerViewAdapter
 *   RxJava: asObservable()
 */
public class RealmUtil {

    private static final int OPERATOR_SUM = 1;
    private static final int OPERATOR_AVERAGE = 2;
    private static final int OPERATOR_MIN = 3;
    private static final int OPERATOR_MAX = 4;
    private static final int OPERATOR_COUNT = 5;

    private static RealmUtil mInstance = null;

    private RealmUtil(){

    }

    public static RealmUtil getInstance(){
        if(mInstance == null){
            synchronized (RealmUtil.class){
                if(mInstance == null){
                    mInstance = new RealmUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * get Realm instance
     * @return mRealm
     */
    public Realm getRealm(){
        return Realm.getDefaultInstance();
    }

    /**
     * invoke close realm in activity or fragment onDestroy to avoid leaking
     */
    public void closeRealm(Realm realm){
        if(!realm.isClosed()){
            realm.close();
        }
    }

    /**
     * through RealmQuery,can construct query of different conditions
     * @param cls class extends RealmObject
     * @param <T> generic paradigm
     * @return RealmQuery of specific class that extends from RealmObject
     * common conditions:
     * equalTo() notEqualTo() between() greaterThan() lessThan()
     * greaterThanOrEqualTo() lessThanOrEqualTo() contains() beginsWith() endsWith()
     * isNull() isNotNull() isEmpty() isNotEmpty() in()
     * sort() -> Sort.ASCENDING or Sort.DESCENDING
     * and() represent login-and,or represent login-or.
     * for example: query.equalTo().or().equalTo()
     */
    public <T extends RealmObject> RealmQuery<T> getRealmQuery(Realm realm, Class<T> cls){
        return realm.where(cls);
    }

    /**
     * add list of data to realm
     * when activity or fragment invoke onDestroy,can invoke RealAsyncTask.cancel
     * @param realmData data for adding
     * @param onSuccess invoke when add successfully
     * @param onError invoke when add failed
     * @return an async task
     */
    public <T extends RealmObject> RealmAsyncTask add(Realm realm, final List<T> realmData,
                                                      Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError){
        return realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(realmData);//have primaryKey
                //realm.copyToRealm(realmData); //no primaryKey
            }
        },onSuccess,onError);
    }

    /**
     * add one data to realm
     * when activity or fragment invoke onDestroy,can invoke RealAsyncTask.cancel
     * @param realmObject data for adding
     * @param onSuccess invoke when add successfully
     * @param onError invoke when add failed
     * @return an async task
     */
    public <T extends RealmObject> RealmAsyncTask add(Realm realm, final T realmObject,
                                                      Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError){
        List<T> realmData = new ArrayList<>(1);
        realmData.add(realmObject);
        return add(realm,realmData,onSuccess,onError);
    }

    /**
     * insert data by json
     * @param cls class extends RealmObject
     * @param json json string
     * @param onSuccess invoke when successfully
     * @param onError invoke when failed
     * @param <T> generic paradigm
     */
    public <T extends RealmObject> void addByJson(Realm realm, final Class<T> cls, final String json,
                                                  Realm.Transaction.OnSuccess onSuccess,
                                                  Realm.Transaction.OnError onError){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(cls,json);
            }
        },onSuccess,onError);
    }

    /**
     * insert data by JsonArray
     * @param cls class extends RealmObject
     * @param jsonArray JsonArray
     * @param onSuccess invoke when successfully
     * @param onError invoke when failed
     * @param <T> generic paradigm
     */
    public <T extends RealmObject> void addByJsonArray(Realm realm, final Class<T> cls, final JSONArray jsonArray,
                                                       Realm.Transaction.OnSuccess onSuccess,
                                                       Realm.Transaction.OnError onError){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(cls,jsonArray);
            }
        },onSuccess,onError);
    }

    /**
     * query data in other thread
     * set conditions by argument of RealmQuery,you can invoke getRealmQuery to attain RealmQuery handler
     * use RxJava(RealmResults.asObservable()) etc. switch to main thread
     * @param <T> generic paradigm
     * @return possible null if use immediately.check for query successfully through RealmChangeListener or isLoaded
     */
    public <T extends RealmObject> RealmResults<T> queryAsync(RealmQuery<T> query){
        return query.findAllAsync();
    }

    /**
     * query data in current thread
     * @param query set conditions by argument of RealmQuery,you can invoke getRealmQuery to attain RealmQuery handler
     * @param <T> generic paradigm
     * @return RealmResults
     */
    public <T extends RealmObject> RealmResults<T> query(RealmQuery<T> query){
        return query.findAll();
    }

    /**
     * query first data in current thread
     * @param query set conditions by argument of RealmQuery,you can invoke getRealmQuery to attain RealmQuery handler
     * @param <T> generic paradigm
     * @return RealmObject
     */
    public <T extends RealmObject> T queryFirst(RealmQuery<T> query){
        return query.findFirst();
    }

    /**
     * update or delete data in certain table
     * query data that is updated or deleted,than update or delete
     * @param transaction some certain operator of update or delete
     * @param onSuccess invoked when succeed
     * @param onError invoked when failed
     * @return an async task
     */
    public RealmAsyncTask updateOrDelete(Realm realm, Realm.Transaction transaction,
                                         Realm.Transaction.OnSuccess onSuccess,
                                         Realm.Transaction.OnError onError){
        return realm.executeTransactionAsync(transaction,onSuccess,onError);
    }

    /**
     * a set of aggregate method,such as: sum,average,max,min,count
     * @param query RealmQuery handler
     * @param operator sum average max min count
     * @param field query for field name,a certain attribute in table
     * @param <T> generic paradigm
     * @return Number,for example,need type of int,you can invoke Number.intValue to type int
     */
    public <T extends RealmObject> Number aggregate(RealmQuery<T> query, int operator, String field){
        Number number = null;
        switch (operator){
            case OPERATOR_SUM:
                number = query.sum(field);
                break;
            case OPERATOR_AVERAGE:
                number = query.average(field);
                break;
            case OPERATOR_MIN:
                number = query.min(field);
                break;
            case OPERATOR_MAX:
                number = query.max(field);
                break;
            case OPERATOR_COUNT:
                number = query.count();
                break;
            default:
                break;
        }
        return number;
    }

}

