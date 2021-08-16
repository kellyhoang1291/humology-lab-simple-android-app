package android.example.humologylabdemo

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.example.humologylabdemo.databinding.FragmentMainBinding
import android.view.*
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item!!,
            requireView()!!.findNavController()
        )
                || super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise(view)
    }

    private fun initialise(v: View) {
        // set database
        val dataHelper = MyDBHelper(v.context)
        val db = dataHelper.readableDatabase
        var rs = db.rawQuery("SELECT * FROM NAMETABLE ORDER BY FULLNAME ASC", null)

        // populate list data
        val adapter = SimpleCursorAdapter(
            context, android.R.layout.simple_expandable_list_item_2, rs,
            arrayOf("FULLNAME", "TIME"),
            intArrayOf(android.R.id.text1, android.R.id.text2), 0
        )
        binding.listView.adapter = adapter

        // search
        binding.searchView.queryHint = getString(R.string.search_view_hint)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                rs = db.rawQuery("SELECT * FROM NAMETABLE WHERE FULLNAME LIKE '%${p0}%'", null)
                adapter.changeCursor(rs)
                return false
            }
        })

        // insert data
        binding.submitButton.setOnClickListener {
            if(binding.fnameText.length() !=0 && binding.lnameText.length() != 0) {
                val cv = ContentValues()
                cv.put("FULLNAME", binding.fnameText.text.toString() + ' ' + binding.lnameText.text.toString())
                db.insert("NAMETABLE", null, cv)
                rs.requery()

                val ad = AlertDialog.Builder(context)
                ad.setTitle(getString(R.string.ad_success_titile))
                ad.setMessage(getString(R.string.ad_success_text))
                ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    binding.fnameText.setText("")
                    binding.lnameText.setText("")
                    binding.fnameText.requestFocus()
                })
                ad.show()
                adapter.notifyDataSetChanged()
                binding.listView.invalidateViews()
            } else {
                Toast.makeText(context, "Please enter your first name and last name!",Toast.LENGTH_LONG).show()
            }
        }
    }

}


//    private fun addInfo(v:View){
//        val helper = MyDBHelper(requireContext())
//
//        // insert data into db
//        binding.submitButton.setOnClickListener {
//            if(binding.fnameText.length() !=0 && binding.lnameText.length() != 0) {
//                val cv = ContentValues()
//                cv.put("FULLNAME", binding.fnameText.text.toString() + ' ' + binding.lnameText.text.toString())
//                db.insert("NAMETABLE", null, cv)
//                rs.requery()
//
//                val ad = AlertDialog.Builder(context)
//                ad.setTitle(getString(R.string.ad_success_titile))
//                ad.setMessage(getString(R.string.ad_success_text))
//                ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
//                    binding.fnameText.setText("")
//                    binding.lnameText.setText("")
//                    binding.fnameText.requestFocus()
//                })
//                ad.show()
//            } else {
//                Toast.makeText(context, "Please enter your first name and last name!",Toast.LENGTH_LONG).show()
//            }
//        }

//
//    }
//}