package com.skylabstechke.todo.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.skylabstechke.todo.R
import com.skylabstechke.todo.data.model.ToDoData
import com.skylabstechke.todo.data.viewmodel.ToDoViewModel
import com.skylabstechke.todo.data.viewmodel.common.ShareViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val mShareViewModel: ShareViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)
        setHasOptionsMenu(true)

        view.current_title_et.setText(args.current.title)
        view.current_descriptions_et.setText(args.current.description)
        view.current_priorities_spinner.setSelection(mShareViewModel.parsePriorityToInt(args.current.priority))
        view.current_priorities_spinner.onItemSelectedListener = mShareViewModel.listener
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.update_save) {
            updateItem()
        }else if(item.itemId == R.id.update_delete){

          confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_->
            mToDoViewModel.delete(args.current)
            Toast.makeText(requireContext(), "Item Deleted", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }


    }

    private fun updateItem() {
        val title = current_title_et.text.toString()
        val description = current_descriptions_et.text.toString()
        val priority = current_priorities_spinner.selectedItem.toString()
        val validation = mShareViewModel.verifyDataFromUser(title, description)
        if (validation) {
            val updateData = ToDoData(
                args.current.id,
                title,
                mShareViewModel.parsePriority(priority),
                description
            )
            mToDoViewModel.updateData(updateData)
            Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }
}