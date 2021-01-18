package de.kdsoftworx.ls_babyalbum2.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class CCollectionPagerAdapter extends FragmentStatePagerAdapter {


    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);

    }

    public CCollectionPagerAdapter(FragmentManager fm) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }



    @Override
    public Fragment getItem(int pagePosition) {
        return fragmentList.get(pagePosition);
    }

    // Number of available Fragments (Tabs)
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    // Give current Position (Tab) back
    @Override
    public CharSequence getPageTitle(int pagePosition) {
        return fragmentTitleList.get(pagePosition);
    }


}
