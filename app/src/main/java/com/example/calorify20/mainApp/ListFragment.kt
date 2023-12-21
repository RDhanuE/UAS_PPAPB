package com.example.calorify20.mainApp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorify20.auth.LogRegScreen
import com.example.calorify20.databinding.FragmentListBinding
import com.example.calorify20.recyclerView.ActivityRvAdapter
import com.example.calorify20.room.CalorifyDatabase
import com.example.calorify20.room.InOutTakeDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var mActivityDao: InOutTakeDao
    private lateinit var executorService: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        executorService = Executors.newSingleThreadExecutor()
        val db = CalorifyDatabase.getDatabase(this.requireActivity())
        mActivityDao = db!!.InOutTakeDao()!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivityDao.getActivtiyByCurrentUser(LogRegScreen.auth.currentUser?.uid.toString()).observe(this.requireActivity()){
            binding.listRv.apply {
                adapter = ActivityRvAdapter(it)
                layoutManager = LinearLayoutManager(this@ListFragment.requireActivity())
            }
        }
        with(binding){
            listAddButton.setOnClickListener {
                val intent = Intent(this@ListFragment.requireActivity(), ChoicesActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
}