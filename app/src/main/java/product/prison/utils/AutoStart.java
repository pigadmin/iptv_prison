package product.prison.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import product.prison.WelcomeActivity;

public class AutoStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			System.out.println("系統开机开机开机");
			 context.startActivity(new Intent(context, WelcomeActivity.class)
					 .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		}else{
			System.out.println("其他广播广播");
		}

	}

}
