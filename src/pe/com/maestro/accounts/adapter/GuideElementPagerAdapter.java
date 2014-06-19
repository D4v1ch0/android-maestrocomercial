package pe.com.maestro.accounts.adapter;

import java.util.List;

import pe.com.maestro.commercial.guide.GuideElementFragment;
import pe.com.maestro.commercial.models.GuideElement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class GuideElementPagerAdapter extends FragmentStatePagerAdapter {
	
	private List<GuideElement> data;
	
	public GuideElementPagerAdapter(FragmentManager fm, List<GuideElement> data) {
		super(fm);
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Fragment getItem(int position) {
		long id = data.get(position).getID();
		return GuideElementFragment.newInstance(id);
	}

}