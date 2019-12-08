package com.example.flipsmart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flipsmart.database.Deck
import com.example.flipsmart.databinding.ListItemDeckBinding

class DeckAdapter(val clickListener : DeckListener): ListAdapter<Deck, DeckAdapter.ViewHolder>(SleepNightDiffCallback()) {
    class ViewHolder private constructor(val binding: ListItemDeckBinding) : RecyclerView.ViewHolder(binding.root){
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDeckBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

        fun bind(
            item: Deck,
            clickListener: DeckListener
        )
        {
           // binding.deckName.text = item.deck
            binding.deck = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item = getItem(position)
        //val res = holder.itemView.context.resources
        holder.bind(item, clickListener)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int) : ViewHolder{
        return ViewHolder.from(parent)
    }


}

class DeckListener(val clickListener: (deckName : String) -> Unit) {
    fun onClick(deck: Deck) = clickListener(deck.deck)
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<Deck>() {
    override fun areItemsTheSame(oldItem: Deck, newItem: Deck): Boolean {
       return oldItem.deck == newItem.deck
    }

    override fun areContentsTheSame(oldItem: Deck, newItem: Deck): Boolean {
        return oldItem.deck == newItem.deck
    }
}