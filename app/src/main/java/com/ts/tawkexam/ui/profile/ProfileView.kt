package com.ts.tawkexam.ui.profile

import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ts.tawkexam.R
import com.ts.tawkexam.data_source.model.User
import com.ts.tawkexam.ui.viewmodel.UserViewModel
import com.ts.tawkexam.ui.viewmodel.UserViewModelFactory
import com.ts.tawkexam.utils.negativeColorFilter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import timber.log.Timber
import javax.inject.Inject

class ProfileView : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: UserViewModelFactory

    private val args: ProfileViewArgs by navArgs()

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)
    }

    private lateinit var user: User

    private var note: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = args.user
        val inverted = args.inverted

        Timber.e("User $user")
        ivBack.onClick { activity?.onBackPressed() }
        tvName.text = user.name ?: getString(R.string.no_name)
        tvFollowerCount.text = user.followers.toString()
        tvFollowingCount.text = user.following.toString()
        tvCompany.text = user.company ?: getString(R.string.no_company)
        tvBlog.text = user.blog ?: getString(R.string.no_blog)
        note = user.note
        etNote.setText(note)
        btnSave.onClick {
            note = etNote.text.toString()
//            viewModel.updateNote(user.id, etNote.text.toString())
            btnSave.isEnabled = false
        }

        if(inverted) {
            civProfileImage.colorFilter = ColorMatrixColorFilter(negativeColorFilter)
        }

        Glide.with(this)
            .load(user.avatarUrl)
            .apply(RequestOptions().error(R.drawable.default_avatar))
            .into(civProfileImage)

//        initEditTextListener()
    }

//    private fun initEditTextListener() {
//        etNote.doAfterTextChanged {
//            val text = it.toString()
//            btnSave.isEnabled = text != user.note
//        }
//    }

    private fun initializeObservers() {
//        viewModel.userOutcome.observe(viewLifecycleOwner, Observer<Outcome<User>> { outcome ->
//            when (outcome) {
    }

}