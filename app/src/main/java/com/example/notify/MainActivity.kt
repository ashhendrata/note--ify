package com.example.notify





import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.notify.ui.theme.NotifyTheme
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.runBlocking
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse

class MainActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerView2: RecyclerView

    private lateinit var mRecyclerViewRe: RecyclerView
    private lateinit var mRecyclerView2Re: RecyclerView

    //    private lateinit var mAdapter: RecyclerView.Adapter<*>
    private lateinit var mAdapter: MyAdapter
    private lateinit var mAdapter2: MyAdapter2

    private lateinit var mAdapterRe: MyAdapterRe
    private lateinit var mAdapter2Re: MyAdapter2Re


    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var mLayoutManagerRe: RecyclerView.LayoutManager

    private lateinit var mLayoutManager2: RecyclerView.LayoutManager
    private lateinit var mLayoutManager2Re: RecyclerView.LayoutManager

    val generativeModel = GenerativeModel(
        // For text-only input, use the gemini-pro model
        modelName = "gemini-pro",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = "AIzaSyA6T1CRI3aTlXWfc8QM7QFpDrW5pbBIlYI"
    )

    suspend fun generate(model: GenerativeModel, prompt: String): GenerateContentResponse {
        return model.generateContent(prompt)
    }

    private lateinit var keys: Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)

        saveToPreferences(this, "last", "")

//        mRecyclerView = findViewById(R.id.my_recyclerview)
//        mRecyclerView2 = findViewById(R.id.my_recyclerview_sec)
//
//        // Use this setting to improve performance if changes in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true)
//
//        // Use a linear layout manager
//        val add_button = findViewById<Button>(R.id.chatbox_send)
//
//
//        mLayoutManager = LinearLayoutManager(this)
//        mLayoutManager2 = LinearLayoutManager(this)
//
//        mRecyclerView.layoutManager = mLayoutManager
//        mRecyclerView2.layoutManager = mLayoutManager2
//
//
//        // Specify an adapter
//        val myDataset = mutableListOf("Data 1", "Data 2", "Data 3")
//
//        mAdapter = MyAdapter(myDataset)
//        mAdapter2 = MyAdapter2(myDataset)
//
//        mRecyclerView.adapter = mAdapter
//        mRecyclerView2.adapter = mAdapter2
//        add_button.setOnClickListener {
//            // Code to execute when the button is clicked
//
//            val editText = findViewById<EditText>(R.id.chatbox)
//            val newText = editText.getText().toString()
//
//            // Modify the text in the EditText
//            editText.setText("")
//            mAdapter.addItem(newText)
//            mAdapter2.addItem("Hey Georgia!")
//        }
        val toggleButton = findViewById<ToggleButton>(R.id.toggleButton)

        val mRecyclerView = findViewById<RecyclerView>(R.id.my_recyclerview)
        val mRecyclerView2 = findViewById<RecyclerView>(R.id.my_recyclerview_sec)

        val mRecyclerViewRe = findViewById<RecyclerView>(R.id.my_recyclerview)
        val mRecyclerView2Re = findViewById<RecyclerView>(R.id.my_recyclerview_sec)

        val editText = findViewById<EditText>(R.id.chatbox)
        val sendButton = findViewById<Button>(R.id.chatbox_send)

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView2.setHasFixedSize(true)
        mRecyclerViewRe.setHasFixedSize(true)
        mRecyclerView2Re.setHasFixedSize(true)

        val mLayoutManager = LinearLayoutManager(this)
        val mLayoutManager2 = LinearLayoutManager(this)

        val mLayoutManagerRe = LinearLayoutManager(this)
        val mLayoutManager2Re = LinearLayoutManager(this)

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView2.layoutManager = mLayoutManager2

//        mRecyclerViewRe.layoutManager = mLayoutManager
//        mRecyclerView2Re.layoutManager = mLayoutManager2

//        val myDataset = mutableListOf(Pair("Data 1", false), Pair("Data 2",true), Pair("Data 3",false))
//        val myDataset2 = mutableListOf(Pair("Data 4", false), Pair("Data 5",true), Pair("Data 6",false))
        val myDataset = mutableListOf<Pair<String, Boolean>>()
        val myDataset2 = mutableListOf<Pair<String, Boolean>>()
        // Separate dataset for independence
        val mAdapter = MyAdapter(myDataset)
//        val mAdapterRe = MyAdapterRe(myDatasetRe)
        val mAdapter2 = MyAdapter2(myDataset2)
//        val mAdapter2Re = MyAdapterRe(myDataset2Re)
        mRecyclerView.adapter = mAdapter
        mRecyclerView2.adapter = mAdapter2

//        mRecyclerViewRe.adapter = mAdapterRe
//        mRecyclerView2Re.adapter = mAdapter2Re

        sendButton.setOnClickListener {
            val newText = editText.text.toString()
            val generativeModel = GenerativeModel(
                // For text-only input, use the gemini-pro model
                modelName = "gemini-pro",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = "AIzaSyA6T1CRI3aTlXWfc8QM7QFpDrW5pbBIlYI"
            )

            suspend fun generate(model: GenerativeModel, prompt: String): GenerateContentResponse {
                return model.generateContent(prompt)
            }





            editText.setText("")  // Clear the EditText

            //api response
            val prompt = "Respond to the following text message acting as a friend. make sure to make it casual and really short at the same time. Don't use emojis. Here is the text you should respond to: " + newText
            runBlocking{
                val response = generate(generativeModel, prompt)
                response.text?.let { it1 -> Log.d("ResponseTag", it1) }
                mAdapter.addItem(newText, false)
                response.text?.let { it1 -> mAdapter.addItem(it1, true) }

//                mAdapter2.addItem(response.toString())
            }
            var newText2 = ""
            if(retrieveFromPreferences(this, "last").toString() == ""){
                newText2 = "Hey, do you have any good movie recommendation?"
            }else{
                newText2 = retrieveFromPreferences(this, "last").toString()
            }
            val prompt2 = "Respond to the following text message acting as a friend. Make sure to make it casual and really short at the same time. Here is the text you should respond to: " + newText2
            var responseText = ""
            runBlocking{
                val response = generate(generativeModel, prompt2)
                responseText = response.text.toString()
                response.text?.let { it1 -> Log.d("ResponseTag", it1) }

                mAdapter2.addItem(newText2, false)
                response.text?.let { it1 -> mAdapter2.addItem(it1, true) }

//                mAdapter2.addItem(response.toString())
            }


            runBlocking{
                val response = generate(generativeModel, prompt2)
                newText2 = response.text.toString()

//                mAdapter2.addItem(response.toString())
            }
            saveToPreferences(this, "last", newText2)









            // Always add a "Hey Georgia" message to the second adapter
        }
        val primaryChat = findViewById<LinearLayout>(R.id.chat_primary)
        val secondaryChat = findViewById<LinearLayout>(R.id.chat_secondary)
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Toggle is enabled - show secondary chat, hide primary chat
                primaryChat.visibility = View.INVISIBLE
                secondaryChat.visibility = View.VISIBLE
            } else {
                // Toggle is disabled - show primary chat, hide secondary chat
                primaryChat.visibility = View.VISIBLE
                secondaryChat.visibility = View.INVISIBLE
            }
        }

    }

}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    NotifyTheme {
//        Greeting("Android")
//    }
//}

class MyAdapter(private val mDataset: MutableList<Pair<String,Boolean >>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textMessage)
        val lay: RelativeLayout = view.findViewById(R.id.messageLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.message, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = mDataset[position].first
        if(mDataset[position].second){
            holder.lay.setBackgroundResource(R.drawable.bubble2)

//            val params = view.layoutParams as RelativeLayout.LayoutParams
//
//            // Remove existing alignment rules if any
//            params.addRule(RelativeLayout.ALIGN_PARENT_END, 0)  // Clear the existing rule first
//
//            // Based on the function argument, add or remove the ALIGN_PARENT_END rule
//            if (shouldBeAlignedToEnd) {
//                params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)  // Align to parent end
//            } else {
//                params.addRule(RelativeLayout.ALIGN_PARENT_END, 0)  // Remove alignment, effectively setting it to false
//            }
//
//            // Set the modified layout parameters back to the view
//            view.layoutParams = params
        }

    }

    override fun getItemCount() = mDataset.size

    fun addItem(item: String, right: Boolean) {
        if(right){
            val newPair = Pair(item, true)
            mDataset.add(newPair)
        }else{
            val newPair = Pair(item, false)
            mDataset.add(newPair)
        }

//        mDataset.add(item)
        notifyItemInserted(mDataset.size - 1)
    }
}

class MyAdapterRe(private val mDataset: MutableList<String>) : RecyclerView.Adapter<MyAdapterRe.ViewHolderRe>() {
    // Define ViewHolder2 correctly inside MyAdapter2
    class ViewHolderRe(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textMessage)
        val lay: LinearLayout = view.findViewById(R.id.messageLayout)
//        val linearLayout:
    }

    // Make sure onCreateViewHolder returns ViewHolder2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRe {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.message, parent, false)
        return ViewHolderRe(v)
    }

    // Ensure onBindViewHolder uses ViewHolder2
    override fun onBindViewHolder(holder: ViewHolderRe, position: Int) {
        holder.textView.text = mDataset[position]
//        holder.lay.layout_alignParentEnd = "false"
//        holder.lay.
    }

    override fun getItemCount() = mDataset.size

    fun addItem(item: String) {
        mDataset.add(item)
        notifyItemInserted(mDataset.size - 1)
    }
}


class MyAdapter2(private val mDataset: MutableList<Pair<String,Boolean >>) : RecyclerView.Adapter<MyAdapter2.ViewHolder2>() {
    // Define ViewHolder2 correctly inside MyAdapter2
    class ViewHolder2(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textMessage)
        val lay: RelativeLayout = view.findViewById(R.id.messageLayout)
//        val linearLayout:
    }

    // Make sure onCreateViewHolder returns ViewHolder2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.message, parent, false)
        return ViewHolder2(v)
    }

    // Ensure onBindViewHolder uses ViewHolder2
    override fun onBindViewHolder(holder: ViewHolder2, position: Int) {

        holder.textView.text = mDataset[position].first
        if(mDataset[position].second) {
            holder.lay.setBackgroundResource(R.drawable.bubble2)
//        holder.lay.
        }
    }

    override fun getItemCount() = mDataset.size

    fun addItem(item: String, right: Boolean) {
        if(right){
            val newPair = Pair(item, true)
            mDataset.add(newPair)
        }else{
            val newPair = Pair(item, false)
            mDataset.add(newPair)
        }

//        mDataset.add(item)
        notifyItemInserted(mDataset.size - 1)
    }
}

















class MyAdapter2Re(private val mDataset: MutableList<String>) : RecyclerView.Adapter<MyAdapter2Re.ViewHolder2Re>() {
    // Define ViewHolder2 correctly inside MyAdapter2
    class ViewHolder2Re(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textMessage)
//        val linearLayout:
    }

    // Make sure onCreateViewHolder returns ViewHolder2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2Re {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.message, parent, false)
        return ViewHolder2Re(v)
    }

    // Ensure onBindViewHolder uses ViewHolder2
    override fun onBindViewHolder(holder: ViewHolder2Re, position: Int) {
        holder.textView.text = mDataset[position]
//        holder.lay.
    }

    override fun getItemCount() = mDataset.size

    fun addItem(item: String) {
        mDataset.add(item)
        notifyItemInserted(mDataset.size - 1)
    }
}
fun saveToPreferences(context: Context, key: String, value: String) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply() // Use apply() to save the data asynchronously
}

fun retrieveFromPreferences(context: Context, key: String): String? {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, null) // Return null if the key doesn't exist
}

//import android.media.MediaPlayer
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.notify.ui.theme.NotifyTheme
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import android.widget.Button
//import android.widget.EditText
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.appcompat.app.AppCompatActivity
//import com.google.ai.client.generativeai.GenerativeModel
//import com.google.ai.client.generativeai.type.GenerateContentResponse
//import kotlinx.coroutines.runBlocking
//import android.util.Log
//import android.view.Gravity
//import android.widget.LinearLayout
//
//
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var mRecyclerView: RecyclerView
//    private lateinit var mRecyclerView2: RecyclerView
//    //    private lateinit var mAdapter: RecyclerView.Adapter<*>
//    private lateinit var mAdapter: MyAdapter
//    private lateinit var mAdapter2: MyAdapter2
//
//
//    private lateinit var mLayoutManager: RecyclerView.LayoutManager
//
//    private lateinit var mLayoutManager2: RecyclerView.LayoutManager
//
//    val generativeModel = GenerativeModel(
//        // For text-only input, use the gemini-pro model
//        modelName = "gemini-pro",
//        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
//        apiKey = "AIzaSyA6T1CRI3aTlXWfc8QM7QFpDrW5pbBIlYI"
//    )
//
//    suspend fun generate(model: GenerativeModel, prompt: String): GenerateContentResponse {
//        return model.generateContent(prompt)
//    }
//
//    private lateinit var keys: Array<Button>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//
//
//        setContentView(R.layout.chat)
//
//        mRecyclerView = findViewById(R.id.my_recyclerview)
//
//        // Use this setting to improve performance if changes in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true)
//
//        // Use a linear layout manager
//        val add_button = findViewById<Button>(R.id.chatbox_send)
//
//
//        mLayoutManager = LinearLayoutManager(this)
//        mRecyclerView.layoutManager = mLayoutManager
//
//        // Specify an adapter
//        val myDataset = mutableListOf("Data 1", "Data 2", "Data 3")
//        mAdapter = MyAdapter(myDataset)
//        mRecyclerView.adapter = mAdapter
//        add_button.setOnClickListener {
//            // Code to execute when the button is clicked
//            val newItem = "New Data 77777" // Generate a new item
//            val editText = findViewById<EditText>(R.id.chatbox)
//            val newText = editText.getText().toString()
//
//            val generativeModel = GenerativeModel(
//                // For text-only input, use the gemini-pro model
//                modelName = "gemini-pro",
//                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
//                apiKey = "AIzaSyA6T1CRI3aTlXWfc8QM7QFpDrW5pbBIlYI"
//            )
//
//            suspend fun generate(model: GenerativeModel, prompt: String): GenerateContentResponse {
//                return model.generateContent(prompt)
//            }
//
//            // Modify the text in the EditText
//            editText.setText("")
//            mAdapter.addItem(newText)
//
//            //api response
//            val prompt = "Respond to the following text message acting as a friend. Make sure to illustrate your concern through your text, however make sure to make it casual and really short at the same time. Don't use emojis. Here is the text you should respond to: " + newText
//            runBlocking{
//                val response = generate(generativeModel, prompt)
//                response.text?.let { it1 -> Log.d("ResponseTag", it1) }
//                response.text?.let { it1 -> mAdapter.addItem(it1) }
//            }
//
//        }
//
//
//
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    NotifyTheme {
//        Greeting("Android")
//    }
//}
//
//class MyAdapter(private val mDataset: MutableList<String>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
//
//    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
//        val textView: TextView = view.findViewById(R.id.textMessage)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val v = inflater.inflate(R.layout.message, parent, false)
//        return ViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.textView.text = mDataset[position]
//    }
//
//    override fun getItemCount() = mDataset.size
//
//    fun addItem(item: String) {
//        mDataset.add(item)
//        notifyItemInserted(mDataset.size - 1)
//    }
//
//}