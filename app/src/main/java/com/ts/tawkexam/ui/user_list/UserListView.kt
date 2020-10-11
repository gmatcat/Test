package com.ts.tawkexam.ui.user_list

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ts.tawkexam.Outcome
import com.ts.tawkexam.R
import com.ts.tawkexam.base.GenericFragment
import com.ts.tawkexam.data_source.model.User
import com.ts.tawkexam.ui.viewmodel.UserViewModel
import com.ts.tawkexam.ui.viewmodel.UserViewModelFactory
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_user_list.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class UserListView : GenericFragment(), UserListCallback {

    @Inject
    lateinit var viewModelFactory: UserViewModelFactory

    lateinit var userListAdapter: UserListAdapter

    lateinit var userListPagingAdapter: UserListPagingAdapter

    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)
    }

    private val mDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        svUser.setOnClickListener {
            svUser.onActionViewExpanded()
        }

//        svUser.setOnQueryTextListener(object :
//            SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                if (newText.isNotEmpty()) {
//                    viewModel.searchUserWithKeyword(newText)
//                } else {
//                    viewModel.getUsers()
//                }
//                return false
//            }
//        })


        userListPagingAdapter = UserListPagingAdapter(requireContext(), this)

        rvUsers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListPagingAdapter
            adapter = userListPagingAdapter.withLoadStateFooter(
                footer = UserListLoadStateAdapter { userListPagingAdapter.retry() }
            )
        }

        userListPagingAdapter.addLoadStateListener { loadState ->
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                AlertDialog.Builder(view.context)
                    .setTitle("ERROR")
                    .setMessage(it.error.localizedMessage)
                    .setNegativeButton("CANCEL") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(R.string.retry) { _, _ ->
                        userListPagingAdapter.retry()
                    }
                    .show()
            }
        }

        mDisposable.add(viewModel.getAllUsers().subscribe {
                userListPagingAdapter.submitData(lifecycle, it)
        })

    }

//    private fun initializeObservers() {
//        viewModel.getA.observe(viewLifecycleOwner, Observer<Outcome<User>> { outcome ->
//            when (outcome) {
////                is Outcome.Progress -> srlPosts.isRefreshing = outcome.loading
//                is Outcome.Success -> {
////                    Log.d(TAG, "initiateDataListener: Successfully loaded data")
//                    Timber.e("addToList " + outcome.data.name)
//                    userListAdapter.addToList(outcome.data)
//                }
////                is Outcome.Failure -> {
////                    if (outcome.e is IOException)
////                        Toast.makeText(
////                            context,
////                            R.string.need_internet_posts,
////                            Toast.LENGTH_LONG
////                        ).show()
////                    else
////                        Toast.makeText(
////                            context,
////                            R.string.failed_post_try_again,
////                            Toast.LENGTH_LONG
////                        ).show()
////                }
//
//            }
//        })
//
//        viewModel.userListOutcome.observe(
//            viewLifecycleOwner,
//            Observer<Outcome<List<User>>> { outcome ->
//                when (outcome) {
//                    is Outcome.Progress -> userListAdapter.clear()
//                    is Outcome.Success -> {
//                        num++
//                        Timber.e("Num $num")
////                    Log.d(TAG, "initiateDataListener: Successfully loaded data")
//                        Timber.e("addUsers " + outcome.data)
//                        userListAdapter.addUsers(outcome.data)
//                    }
////                is Outcome.Failure -> {
////                    if (outcome.e is IOException)
////                        Toast.makeText(
////                            context,
////                            R.string.need_internet_posts,
////                            Toast.LENGTH_LONG
////                        ).show()
////                    else
////                        Toast.makeText(
////                            context,
////                            R.string.failed_post_try_again,
////                            Toast.LENGTH_LONG
////                        ).show()
////                }
//
//                }
//            })
//    }

    override fun onClickUser(user: User, inverted: Boolean) {
        val action = UserListViewDirections.actionUserListViewToProfileView(user, inverted)
        findNavController().navigate(action)
    }

    override fun onInternetConnect() {
//        viewModel.getUsers()
    }

    override fun onInternetDisconnect() {
//        TODO("Not yet implemented")
    }

}