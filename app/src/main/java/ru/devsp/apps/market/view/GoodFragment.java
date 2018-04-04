package ru.devsp.apps.market.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import javax.inject.Inject;

import ru.devsp.apps.market.R;
import ru.devsp.apps.market.model.api.tools.Resource;
import ru.devsp.apps.market.model.api.tools.Status;
import ru.devsp.apps.market.model.objects.Good;
import ru.devsp.apps.market.view.comopnents.CircleIndicator;
import ru.devsp.apps.market.view.comopnents.ImagePagerAdapter;
import ru.devsp.apps.market.viewmodel.GoodsViewModel;

/**
 * Карточка товара
 * Created by gen on 22.12.2017.
 */

public class GoodFragment extends BaseFragment {

    private static final String ARG_GOOD_ID = "id";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ViewPager mGoodImages;
    private Button mAddToCart;
    private CircleIndicator mPageIndicator;

    private TextView mGoodDesc;
    private TextView mGoodTitle;
    private TextView mGoodPrice;

    private Good mGood;

    public static GoodFragment getInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(ARG_GOOD_ID, id);
        GoodFragment fragment = new GoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_good, container, false);
        initComponent();

        mGoodImages = view.findViewById(R.id.vp_good_images);
        mPageIndicator = view.findViewById(R.id.ci_good_images_pages);
        mAddToCart = view.findViewById(R.id.btn_add_to_cart);
        mGoodDesc = view.findViewById(R.id.tv_good_desc);
        mGoodTitle = view.findViewById(R.id.tv_good_title);
        mGoodPrice = view.findViewById(R.id.tv_good_price);

        setHasOptionsMenu(false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateToolbar();

        GoodsViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(GoodsViewModel.class);

        viewModel.getGood().observe(this, resource -> {
            if (resource != null) {
                processResult(resource);
            }
        });

        viewModel.getImages().observe(this, images -> {
            //Изображения
            ImagePagerAdapter adapter = new ImagePagerAdapter(getChildFragmentManager(), images);
            mGoodImages.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mPageIndicator.setViewPager(mGoodImages);
        });

        viewModel.setGood(getArguments().getLong(ARG_GOOD_ID));

        mAddToCart.setOnClickListener(v -> addToCart(viewModel));
    }

    private void addToCart(GoodsViewModel viewModel) {
        if (mGood != null) {
            viewModel.addToCart(mGood, 1).observe(this, id ->
                    showSnack("Добавлено в корзину", v -> {
                        if (id != null) {
                            viewModel.deleteFromCart(id);
                        }
                    }));
        }
    }

    private void processResult(Resource<Good> resource) {
        if (resource.status == Status.SUCCESS && resource.data != null) {
            //обновление данных
            mGood = resource.data;
            mAddToCart.setEnabled(true);
            updateTitle(mGood.title);
            mGoodDesc.setText(mGood.desc);
            mGoodTitle.setText(mGood.title);
            mGoodPrice.setText(String.format(Locale.getDefault(), getString(R.string.pattern_price), mGood.price));
        }
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @Override
    protected String getTitle() {
        return "";
    }

}
