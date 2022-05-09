package com.penelope.seatforyou.ui.manager.shop.shop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.penelope.seatforyou.R;
import com.penelope.seatforyou.data.address.Address;
import com.penelope.seatforyou.databinding.DialogPhoneBinding;
import com.penelope.seatforyou.databinding.FragmentShopBinding;
import com.penelope.seatforyou.utils.BitmapUtils;
import com.penelope.seatforyou.utils.StringUtils;
import com.penelope.seatforyou.utils.TimeUtils;
import com.penelope.seatforyou.utils.ui.OnTextChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ShopFragment extends Fragment {

    private FragmentShopBinding binding;
    private ShopViewModel viewModel;

    private ActivityResultLauncher<Intent> imageActivityLauncher;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;


    public ShopFragment() {
        super(R.layout.fragment_shop);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null) {
                        // 비트맵 획득
                        Bitmap bitmap = null;
                        if (result.getData().getExtras() != null) {
                            // 카메라 결과 획득
                            bitmap = (Bitmap) result.getData().getExtras().get("data");
                        } else {
                            // 갤러리(포토) 결과 획득
                            Uri uri = result.getData().getData();
                            if (uri != null) {
                                String path = BitmapUtils.getRealPathFromUri(requireContext(), uri);
                                bitmap = BitmapUtils.getBitmapFromPath(path);
                            }
                        }
                        viewModel.onImageSelected(bitmap);
                    }
                }
        );

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean permissionExternalStorage = result.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                    Boolean permissionCamera = result.get(Manifest.permission.CAMERA);

                    if (permissionExternalStorage != null && permissionExternalStorage
                            && permissionCamera != null && permissionCamera) {
                        showImageDialog();
                    }
                }
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentShopBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        // 버튼 클릭 시 뷰모델에 통보
        binding.buttonShopName.setOnClickListener(v -> viewModel.onSetNameClick());
        binding.buttonShopDescription.setOnClickListener(v -> viewModel.onSetDescriptionClick());
        binding.buttonShopImage.setOnClickListener(v -> viewModel.onSetImageClick());
        binding.buttonPhone.setOnClickListener(v -> viewModel.onSetPhoneClick());
        binding.buttonCategories.setOnClickListener(v -> viewModel.onSetMenusClick());
        binding.buttonAddress.setOnClickListener(v -> viewModel.onSetAddressClick());
        binding.buttonCategories.setOnClickListener(v -> viewModel.onSetCategoriesClick());
        binding.buttonOpenClose.setOnClickListener(v -> viewModel.onSetOpenCloseClick());
        binding.buttonMenus.setOnClickListener(v -> viewModel.onSetMenusClick());
        binding.fabSubmit.setOnClickListener(v -> viewModel.onSubmitClick());

        // EditText 입력 시 뷰모델에 통보
        binding.editTextShopName.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onNameInputChange(text);
            }
        });
        binding.editTextShopDescription.addTextChangedListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewModel.onDescriptionInputChange(text);
            }
        });

        // 가게 이름 업데이트
        viewModel.getName().observe(getViewLifecycleOwner(), name ->
                binding.textViewShopName.setText(name));

        // 가게 설명 업데이트
        viewModel.getDescription().observe(getViewLifecycleOwner(), desc ->
                binding.textViewShopDescription.setText(desc));

        // 가게 메뉴 업데이트
        viewModel.getMenus().observe(getViewLifecycleOwner(), menus -> {
            if (menus != null) {
                List<String> names = new ArrayList<>(menus.keySet());
                binding.textViewMenus.setText(StringUtils.concat(names, ", "));
            }
        });

        // 가게 주소 업데이트
        viewModel.getAddress().observe(getViewLifecycleOwner(), address -> {
            String fullAddress = address.getLoadAddress() + " " + address.getDetail();
            binding.textViewShopAddress.setText(fullAddress);
        });

        // 가게 전화번호 업데이트
        viewModel.getPhone().observe(getViewLifecycleOwner(), phone ->
                binding.textViewShopPhone.setText(StringUtils.phone(phone)));

        // 가게 카테고리 업데이트
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                binding.textViewCategories.setText(StringUtils.concat(categories, ", "));
            }
        });

        // 가게 영업시간 업데이트
        viewModel.getOpenHour().observe(getViewLifecycleOwner(), openHour ->
                viewModel.getOpenMinute().observe(getViewLifecycleOwner(), openMinute ->
                        viewModel.getCloseHour().observe(getViewLifecycleOwner(), closeHour ->
                                viewModel.getCloseMinute().observe(getViewLifecycleOwner(), closeMinute -> {
                                    String strOpenClose = TimeUtils.formatTime(openHour, openMinute, closeHour, closeMinute);
                                    binding.textViewOpenClose.setText(strOpenClose);
                                }))));

        // 가게 이미지 업데이트
        viewModel.getImage().observe(getViewLifecycleOwner(), image ->
                binding.imageViewShopImage.setImageBitmap(image));

        // 가게 이름 편집모드 업데이트
        viewModel.isSettingName().observe(getViewLifecycleOwner(), isSettingName -> {
            binding.cardShopName.setVisibility(isSettingName ? View.VISIBLE : View.GONE);
            binding.buttonShopName.setText(isSettingName ? "✓" : "+");
            if (isSettingName) {
                binding.editTextShopName.requestFocus();
            }
        });

        // 가게 설명 편집모드 업데이트
        viewModel.isSettingDescription().observe(getViewLifecycleOwner(), isSettingDescription -> {
            binding.cardShopDescription.setVisibility(isSettingDescription ? View.VISIBLE : View.GONE);
            binding.buttonShopDescription.setText(isSettingDescription ? "✓" : "+");
            if (isSettingDescription) {
                binding.editTextShopDescription.requestFocus();
            }
        });

        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof ShopViewModel.Event.ShowGeneralMessage) {
                String message = ((ShopViewModel.Event.ShowGeneralMessage) event).message;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else if (event instanceof ShopViewModel.Event.PromptImage) {
                promptImage();
            } else if (event instanceof ShopViewModel.Event.PromptPhone) {
                String phone = ((ShopViewModel.Event.PromptPhone) event).phone;
                promptPhone(phone);
            } else if (event instanceof ShopViewModel.Event.NavigateToAddressScreen) {
                Address oldAddress = ((ShopViewModel.Event.NavigateToAddressScreen) event).oldAddress;
                NavDirections navDirections = ShopFragmentDirections.actionShopFragmentToAddressFragment(oldAddress);
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof ShopViewModel.Event.NavigateToOpenCloseScreen) {
                int openHour = ((ShopViewModel.Event.NavigateToOpenCloseScreen) event).openHour;
                int openMinute = ((ShopViewModel.Event.NavigateToOpenCloseScreen) event).openMinute;
                int closeHour = ((ShopViewModel.Event.NavigateToOpenCloseScreen) event).closeHour;
                int closeMinute = ((ShopViewModel.Event.NavigateToOpenCloseScreen) event).closeMinute;
                NavDirections navDirections = ShopFragmentDirections.actionShopFragmentToOpenCloseFragment(
                        openHour, openMinute, closeHour, closeMinute
                );
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof ShopViewModel.Event.NavigateToCategoryScreen) {
                List<String> categories = ((ShopViewModel.Event.NavigateToCategoryScreen) event).categories;
                NavDirections navDirections = ShopFragmentDirections.actionShopFragmentToCategoryFragment(new ArrayList<>(categories));
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof ShopViewModel.Event.NavigateToMenuScreen) {
                Map<String, Integer> menus = ((ShopViewModel.Event.NavigateToMenuScreen) event).menus;
                NavDirections navDirections = ShopFragmentDirections.actionShopFragmentToMenuFragment(new HashMap<>(menus));
                Navigation.findNavController(requireView()).navigate(navDirections);
            } else if (event instanceof ShopViewModel.Event.NavigateBackWithResult) {
                boolean enrollOrModify = ((ShopViewModel.Event.NavigateBackWithResult) event).enrollOrModify;
                Bundle result = new Bundle();
                result.putBoolean("enroll_or_modify", enrollOrModify);
                getParentFragmentManager().setFragmentResult("shop_fragment", result);
                Navigation.findNavController(requireView()).popBackStack();
            } else if (event instanceof ShopViewModel.Event.HideLoadingUI) {
                binding.progressBar2.setVisibility(View.INVISIBLE);
            }
        });

        getParentFragmentManager().setFragmentResultListener("address_fragment", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    Address address = (Address) result.getSerializable("address");
                    viewModel.onAddressResult(address);
                });

        getParentFragmentManager().setFragmentResultListener("open_close_fragment", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    int openHour = result.getInt("open_hour");
                    int openMinute = result.getInt("open_minute");
                    int closeHour = result.getInt("close_hour");
                    int closeMinute = result.getInt("close_minute");
                    viewModel.onOpenCloseResult(openHour, openMinute, closeHour, closeMinute);
                });

        getParentFragmentManager().setFragmentResultListener("category_fragment", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    List<String> categories = result.getStringArrayList("categories");
                    viewModel.onCategoriesResult(categories);
                });

        getParentFragmentManager().setFragmentResultListener("menu_fragment", getViewLifecycleOwner(),
                (requestKey, result) -> {
                    List<String> menuNames = result.getStringArrayList("menu_names");
                    List<Integer> menuPrices = result.getIntegerArrayList("menu_prices");
                    viewModel.onMenusResult(menuNames, menuPrices);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void promptImage() {

        if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && requireContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showImageDialog();
        } else {
            requestPermissionLauncher.launch(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}
            );
        }
    }

    private void showImageDialog() {

        // 업로드 방법 선택 대화상자 보이기
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooser = Intent.createChooser(galleryIntent, "사진 업로드");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        imageActivityLauncher.launch(chooser);
    }

    private void promptPhone(String phone) {

        DialogPhoneBinding binding = DialogPhoneBinding.inflate(getLayoutInflater());
        binding.editTextShopPhone.setText(phone);

        new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setPositiveButton("확인", (dialog, which) -> {
                    String value = binding.editTextShopPhone.getText().toString();
                    viewModel.onPhoneSelected(value);
                })
                .setNegativeButton("취소", null)
                .show();
    }

}




