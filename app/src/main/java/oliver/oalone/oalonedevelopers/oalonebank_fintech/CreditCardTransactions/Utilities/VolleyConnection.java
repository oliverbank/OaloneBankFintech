package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.Utilities;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyConnection {

    private static VolleyConnection mVolleyS = null;
    private RequestQueue mRequestQueue;

    public VolleyConnection(Context ctx){
        mRequestQueue = Volley.newRequestQueue(ctx);
    }

    public static VolleyConnection getInstance(Context ctx) {
        if (mVolleyS == null) {
            mVolleyS = new VolleyConnection(ctx);
        }
        return mVolleyS;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

}
