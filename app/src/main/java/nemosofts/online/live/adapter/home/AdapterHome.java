package nemosofts.online.live.adapter.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.nemosofts.view.enchanted.EnchantedViewPager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import nemosofts.online.live.R;
import nemosofts.online.live.activity.MainActivity;
import nemosofts.online.live.activity.PostIDActivity;
import nemosofts.online.live.activity.VideoDetailsActivity;
import nemosofts.online.live.callback.Callback;
import nemosofts.online.live.fragment.online.FragmentCategories;
import nemosofts.online.live.fragment.online.FragmentEvent;
import nemosofts.online.live.fragment.online.FragmentLatest;
import nemosofts.online.live.fragment.online.FragmentRecent;
import nemosofts.online.live.fragment.online.FragmentSectionLive;
import nemosofts.online.live.fragment.online.FragmentTrending;
import nemosofts.online.live.interfaces.InterAdListener;
import nemosofts.online.live.item.ItemPost;
import nemosofts.online.live.utils.helper.Helper;
import nemosofts.online.live.utils.helper.SPHelper;
import nemosofts.online.live.utils.recycler.RecyclerItemClickListener;

public class AdapterHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    Helper helper;
    SPHelper spHelper;
    List<ItemPost> arrayList;
    int clickPos = 0;

    private static final int VIEW_PROG = 0;
    private static final int VIEW_BANNER = 1;
    private static final int VIEW_CATEGORIES = 2;
    private static final int VIEW_EVENT = 3;
    private static final int VIEW_LIVE = 4;
    private static final int VIEW_RECENT = 5;
    private static final int VIEW_ADS = 6;

    Boolean ads = true;

    public AdapterHome(Context context, List<ItemPost> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        helper = new Helper(context, interAdListener);
        spHelper = new SPHelper(context);
    }

    static class BannerHolder extends RecyclerView.ViewHolder {

        EnchantedViewPager enchantedViewPager;
        HomePagerAdapter homePagerAdapter;

        BannerHolder(View view) {
            super(view);
            enchantedViewPager = view.findViewById(R.id.viewPager_home);
            enchantedViewPager.useAlpha();
            enchantedViewPager.useScale();
            enchantedViewPager.setPageMargin(-5);
        }
    }

    class CategoriesHolder extends RecyclerView.ViewHolder {

        RecyclerView rv;
        AdapterHomeCategories adapterHomeCategories;
        TextView title;
        LinearLayout viewAll;

        CategoriesHolder(View view) {
            super(view);
            rv = view.findViewById(R.id.rv_home_cat);
            title = view.findViewById(R.id.tv_home_title);
            viewAll = view.findViewById(R.id.ll_home_view_all);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rv.setLayoutManager(linearLayoutManager);
            rv.setItemAnimator(new DefaultItemAnimator());
        }
    }

    class EventHolder extends RecyclerView.ViewHolder {

        RecyclerView rv;
        AdapterHomeEvent adapter;
        TextView title;
        LinearLayout viewAll;

        EventHolder(View view) {
            super(view);
            rv = view.findViewById(R.id.rv_home_cat);
            title = view.findViewById(R.id.tv_home_title);
            viewAll = view.findViewById(R.id.ll_home_view_all);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            rv.setLayoutManager(linearLayoutManager);
            rv.setItemAnimator(new DefaultItemAnimator());
        }
    }

    class LiveHolder extends RecyclerView.ViewHolder {

        RecyclerView rv;
        AdapterHomeLive adapter;
        TextView title;
        LinearLayout viewAll;

        LiveHolder(View view) {
            super(view);
            rv = view.findViewById(R.id.rv_home_cat);
            title = view.findViewById(R.id.tv_home_title);
            viewAll = view.findViewById(R.id.ll_home_view_all);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rv.setLayoutManager(linearLayoutManager);
            rv.setItemAnimator(new DefaultItemAnimator());
        }
    }

    class RecentHolder extends RecyclerView.ViewHolder {

        RecyclerView rv;
        TextView title;
        AdapterHomeRecent adapter;
        LinearLayout viewAll;

        RecentHolder(View view) {
            super(view);
            rv = view.findViewById(R.id.rv_home_cat);
            title = view.findViewById(R.id.tv_home_title);
            viewAll = view.findViewById(R.id.ll_home_view_all);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rv.setLayoutManager(linearLayoutManager);
            rv.setItemAnimator(new DefaultItemAnimator());
        }
    }

    static class LatestAds extends RecyclerView.ViewHolder {

        LinearLayout adView;

        LatestAds(View view) {
            super(view);
            adView = view.findViewById(R.id.ll_adView);
        }
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {

        @SuppressLint("StaticFieldLeak")
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_BANNER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_home_ui_banner, parent, false);
            return new BannerHolder(itemView);
        } else if (viewType == VIEW_CATEGORIES) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_home_ui_categories, parent, false);
            return new CategoriesHolder(itemView);
        } else if (viewType == VIEW_EVENT) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_home_ui_categories, parent, false);
            return new EventHolder(itemView);
        } else if (viewType == VIEW_LIVE) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_home_ui_categories, parent, false);
            return new LiveHolder(itemView);
        }  else if (viewType == VIEW_RECENT) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_home_ui_categories, parent, false);
            return new RecentHolder(itemView);
        } else if (viewType == VIEW_ADS) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_baner_ad, parent, false);
            return new LatestAds(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_progressbar, parent, false);
            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BannerHolder bannerHolder) {
            if (bannerHolder.homePagerAdapter == null) {
                bannerHolder.enchantedViewPager.setFocusable(false);
                bannerHolder.homePagerAdapter = new HomePagerAdapter(context,
                        arrayList.get(holder.getAbsoluteAdapterPosition()).getArrayListBanner());
                bannerHolder.enchantedViewPager.setAdapter(((BannerHolder) holder).homePagerAdapter);
                if (bannerHolder.homePagerAdapter.getCount() > 2) {
                    bannerHolder.enchantedViewPager.setCurrentItem(1);
                }
            }
        }
        else if (holder instanceof RecentHolder recentHolder) {
            recentHolder.title.setText(arrayList.get(holder.getAbsoluteAdapterPosition()).getTitle());

            recentHolder.adapter = new AdapterHomeRecent(context,
                    arrayList.get(holder.getAbsoluteAdapterPosition()).getArrayListLive(), new AdapterHomeRecent.RecyclerItemClickListener() {
                @Override
                public void onClick(int position) {
                    clickPos = holder.getAbsoluteAdapterPosition();
                    helper.showInterAd(position, context.getString(R.string.live));
                }

                @Override
                public void onRewardAds(int position) {
                    if (spHelper.getRewardCredit() != 0){
                        spHelper.useRewardCredit(1);
                        clickPos = holder.getAbsoluteAdapterPosition();
                        helper.showInterAd(position, context.getString(R.string.live));
                        Toast.makeText(context, "Your Total Credit ("+spHelper.getRewardCredit()+")", Toast.LENGTH_SHORT).show();
                    } else {
                        helper.showRewardAds(holder.getAbsoluteAdapterPosition(), (isLoad, pos) -> {
                            if (isLoad){
                                spHelper.addRewardCredit(Callback.getRewardCredit());
                                spHelper.useRewardCredit(1);
                                clickPos = pos;
                                helper.showInterAd(pos, context.getString(R.string.live));
                                Toast.makeText(context, "Your Total Credit ("+spHelper.getRewardCredit()+")", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Display Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            recentHolder.rv.setAdapter(recentHolder.adapter);

            recentHolder.viewAll.setOnClickListener(v -> {
                FragmentRecent recent = new FragmentRecent();
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.hide(((AppCompatActivity) context).getSupportFragmentManager()
                        .getFragments()
                        .get(((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount()));
                ft.add(R.id.fragment, recent, context.getString(R.string.recently));
                ft.addToBackStack(context.getString(R.string.recently));
                ft.commit();
                Objects.requireNonNull(((MainActivity) context).getSupportActionBar()).setTitle(context.getString(R.string.recently));
                ((MainActivity) context).bottomNavigationView(4);
            });
        }
        else if (holder instanceof CategoriesHolder categoriesHolder) {

            categoriesHolder.title.setText(arrayList.get(holder.getAbsoluteAdapterPosition()).getTitle());

            categoriesHolder.adapterHomeCategories = new AdapterHomeCategories(
                    arrayList.get(holder.getAbsoluteAdapterPosition()).getArrayListCategories());
            categoriesHolder.rv.setAdapter(categoriesHolder.adapterHomeCategories);

            categoriesHolder.rv.addOnItemTouchListener(new RecyclerItemClickListener(context, (view, position1) -> {
                clickPos = holder.getAbsoluteAdapterPosition();
                helper.showInterAd(position1, context.getString(R.string.categories));
            }));

            categoriesHolder.viewAll.setOnClickListener(v -> {
                FragmentCategories albums = new FragmentCategories();
                Bundle bundle = new Bundle();
                bundle.putBoolean("ishome", true);
                bundle.putString("id", arrayList.get(holder.getAbsoluteAdapterPosition()).getId());
                albums.setArguments(bundle);
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.hide(((AppCompatActivity) context).getSupportFragmentManager()
                        .getFragments()
                        .get(((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount()));
                ft.add(R.id.fragment, albums, context.getString(R.string.categories));
                ft.addToBackStack(context.getString(R.string.categories));
                ft.commit();
                Objects.requireNonNull(((MainActivity) context).getSupportActionBar()).setTitle(
                        arrayList.get(holder.getAbsoluteAdapterPosition()).getTitle());
                ((MainActivity) context).bottomNavigationView(5);
            });
        }
        else if (holder instanceof EventHolder eventHolder) {
            eventHolder.title.setText(arrayList.get(holder.getAbsoluteAdapterPosition()).getTitle());

            eventHolder.adapter = new AdapterHomeEvent(arrayList.get(holder.getAbsoluteAdapterPosition()).getArrayListEvent());
            eventHolder.rv.setAdapter(eventHolder.adapter);

            eventHolder.rv.addOnItemTouchListener(new RecyclerItemClickListener(context, (view, position1) -> {
                clickPos = holder.getAbsoluteAdapterPosition();
                helper.showInterAd(position1, context.getString(R.string.live_event));
            }));

            eventHolder.viewAll.setOnClickListener(v -> {
                FragmentEvent event = new FragmentEvent();
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.hide(((AppCompatActivity) context).getSupportFragmentManager()
                        .getFragments()
                        .get(((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount()));
                ft.add(R.id.fragment, event, context.getString(R.string.live_event));
                ft.addToBackStack(context.getString(R.string.live_event));
                ft.commit();
                Objects.requireNonNull(((MainActivity) context).getSupportActionBar()).setTitle(context.getString(R.string.live_event));
                ((MainActivity) context).bottomNavigationView(5);
            });
        }
        else if (holder instanceof LiveHolder liveHolder) {
            liveHolder.title.setText(arrayList.get(holder.getAbsoluteAdapterPosition()).getTitle());

            liveHolder.adapter = new AdapterHomeLive(context,
                    arrayList.get(holder.getAbsoluteAdapterPosition()).getArrayListLive(), new AdapterHomeLive.RecyclerItemClickListener() {
                @Override
                public void onClick(int position) {
                    clickPos = holder.getAbsoluteAdapterPosition();
                    helper.showInterAd(position, context.getString(R.string.live));
                }

                @Override
                public void onRewardAds(int position) {
                    if (spHelper.getRewardCredit() != 0){
                        spHelper.useRewardCredit(1);
                        clickPos = holder.getAbsoluteAdapterPosition();
                        helper.showInterAd(position, context.getString(R.string.live));
                        Toast.makeText(context, "Your Total Credit ("+spHelper.getRewardCredit()+")", Toast.LENGTH_SHORT).show();
                    } else {
                        helper.showRewardAds(holder.getAbsoluteAdapterPosition(), (isLoad, pos) -> {
                            if (isLoad){
                                spHelper.addRewardCredit(Callback.getRewardCredit());
                                spHelper.useRewardCredit(1);
                                clickPos = pos;
                                helper.showInterAd(pos, context.getString(R.string.live));
                                Toast.makeText(context, "Your Total Credit ("+spHelper.getRewardCredit()+")", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Display Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            liveHolder.rv.setAdapter(liveHolder.adapter);

            liveHolder.viewAll.setOnClickListener(v -> {
                if (arrayList.get(holder.getAbsoluteAdapterPosition()).getSections().equals("recently")){
                    FragmentRecent recently = new FragmentRecent();
                    FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.hide(((AppCompatActivity) context).getSupportFragmentManager()
                            .getFragments()
                            .get(((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount()));
                    ft.add(R.id.fragment, recently, context.getString(R.string.recently));
                    ft.addToBackStack(context.getString(R.string.recently));
                    ft.commit();
                    Objects.requireNonNull(((MainActivity) context).getSupportActionBar()).setTitle(context.getString(R.string.recently));
                    ((MainActivity) context).bottomNavigationView(4);
                } else if (arrayList.get(holder.getAbsoluteAdapterPosition()).getSections().equals("latest")){
                    FragmentLatest latest = new FragmentLatest();
                    FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.hide(((AppCompatActivity) context).getSupportFragmentManager()
                            .getFragments()
                            .get(((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount()));
                    ft.add(R.id.fragment, latest, context.getString(R.string.latest));
                    ft.addToBackStack(context.getString(R.string.latest));
                    ft.commit();
                    Objects.requireNonNull(((MainActivity) context).getSupportActionBar()).setTitle(context.getString(R.string.latest));
                    ((MainActivity) context).bottomNavigationView(1);
                } else if (arrayList.get(holder.getAbsoluteAdapterPosition()).getSections().equals("trending")){
                    FragmentTrending most = new FragmentTrending();
                    FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.hide(((AppCompatActivity) context).getSupportFragmentManager()
                            .getFragments()
                            .get(((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount()));
                    ft.add(R.id.fragment, most, context.getString(R.string.trending));
                    ft.addToBackStack(context.getString(R.string.trending));
                    ft.commit();
                    Objects.requireNonNull(((MainActivity) context).getSupportActionBar()).setTitle(context.getString(R.string.trending));
                    ((MainActivity) context).bottomNavigationView(2);
                } else {
                    FragmentSectionLive section = new FragmentSectionLive();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", arrayList.get(holder.getAbsoluteAdapterPosition()).getId());
                    section.setArguments(bundle);
                    FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.hide(((AppCompatActivity) context).getSupportFragmentManager()
                            .getFragments()
                            .get(((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount()));
                    ft.add(R.id.fragment, section, context.getString(R.string.live));
                    ft.addToBackStack(context.getString(R.string.live));
                    ft.commit();
                    Objects.requireNonNull(((MainActivity) context).getSupportActionBar()).setTitle(((LiveHolder) holder).title.getText().toString());
                    ((MainActivity) context).bottomNavigationView(5);
                }
            });
        }
        else if (holder instanceof LatestAds latestAds) {
            if (Boolean.TRUE.equals(ads)){
                ads = false;
                helper.showBannerAd(latestAds.adView, Callback.PAGE_HOME);
            }
        }
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void hideHeader() {
        ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }

    public boolean isHeader(int position) {
        return arrayList.get(position) == null;
    }

    @Override
    public int getItemViewType(int position) {
        return switch (arrayList.get(position).getType()) {
            case "slider" -> VIEW_BANNER;
            case "live" -> VIEW_LIVE;
            case "recent" -> VIEW_RECENT;
            case "category" -> VIEW_CATEGORIES;
            case "event" -> VIEW_EVENT;
            case "ads" -> VIEW_ADS;
            default -> VIEW_PROG;
        };
    }

    InterAdListener interAdListener = (position, type) -> {
        if (type.equals(context.getString(R.string.live))) {
            Intent intent = new Intent(context, VideoDetailsActivity.class);
            intent.putExtra("post_id", arrayList.get(clickPos).getArrayListLive().get(position).getId());
            context.startActivity(intent);
        } else if (type.equals(context.getString(R.string.categories))){
            Intent intent = new Intent(context, PostIDActivity.class);
            intent.putExtra("page_type", context.getString(R.string.categories));
            intent.putExtra("id", arrayList.get(clickPos).getArrayListCategories().get(position).getId());
            intent.putExtra("name", arrayList.get(clickPos).getArrayListCategories().get(position).getName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if (type.equals(context.getString(R.string.live_event))) {
            Intent intent = new Intent(context, VideoDetailsActivity.class);
            intent.putExtra("post_id", arrayList.get(clickPos).getArrayListEvent().get(position).getPostId());
            context.startActivity(intent);
        }
    };
}