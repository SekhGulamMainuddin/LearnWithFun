package com.sekhgmainuddin.learnwithfun.ui.home.student.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sekhgmainuddin.learnwithfun.data.modals.MyLearnings
import com.sekhgmainuddin.learnwithfun.databinding.FragmentHomeBinding
import com.sekhgmainuddin.learnwithfun.ui.home.student.LearnViewModel
import com.sekhgmainuddin.learnwithfun.ui.home.student.home.adapters.MyLearningsAdapter
import com.sekhgmainuddin.learnwithfun.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding?= null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private val viewModel by activityViewModels<LearnViewModel>()
    private lateinit var myLearningsAdapter: MyLearningsAdapter
    private val bannerItem= MyLearnings("",-101,-101,-101)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentHomeBinding.inflate(inflater)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBanners()

        myLearningsAdapter= MyLearningsAdapter(requireContext())
        binding.coursesTakenRV.adapter= myLearningsAdapter
        myLearningsAdapter.submitList(listOf(bannerItem))

        bindObserver()

    }

    private fun bindObserver(){
        viewModel.banners.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success->{
                    it.data?.let { it1 -> myLearningsAdapter.updateBanner(it1) }
                }
                is NetworkResult.Error->{

                }
                is NetworkResult.Loading->{

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }

}