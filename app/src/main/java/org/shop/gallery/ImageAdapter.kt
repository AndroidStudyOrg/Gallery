package org.shop.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.shop.gallery.databinding.ItemImageBinding
import org.shop.gallery.databinding.ItemLoadMoreBinding

class ImageAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<ImageItems, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<ImageItems>() {
            // 같은 데이터 객체를 참조하고 있는지
            override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
                return oldItem === newItem
            }

            // 정말 같은 데이터인지
            override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    /** @see
     * 맨 마지막에 아이템으로 LoadMore을 넣어줌. 이를 보통 putter라고 함.
     * 이를 맨 앞에 넣으면 Header, 맨 뒤에 넣으면 Putter라고 한다.
     *
     * 가정)
     * 데이터가 하나라도 있으면 그 뒤에 '더보기' 아이템이 붙는다.
     */
    override fun getItemCount(): Int {
        /**
         * originSize: 현재 가지고 있는 데이터의 사이즈
         */
        val originSize = currentList.size
        /**
         * originSize가 0이면 넣을 putter도 없다. 하나라도 있을 시 putter를 추가.(+1)
         */
        return if (originSize == 0) 0 else originSize.inc()
    }

    /** @see
     * 타입을 여러가지로 가져감. 두가지 타입이 있으므로 타입체크를 해줄 필요가 있다.
     * ITEM_IMAGE / ITEM_LOAD_MORE 이 두가지 타입을 선언해놓고 getItemViewType()를 구현한다.
     * itemCount가 0일 때에는 고려해줄 필요가 없음. 하나라도 있을 때부터 확인.
     * itemCount-1 이 Position이면 putter. 그게 아니라면 ITEM_IMAGE
     * ex) 이미지1, 이미지2, putter 가 있으면 itemCount = 3, putter의 position은 2
     */
    override fun getItemViewType(position: Int): Int {
        return if (itemCount.dec() == position) ITEM_LOAD_MORE else ITEM_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when (viewType) {
            ITEM_IMAGE -> {
                val binding = ItemImageBinding.inflate(inflater, parent, false)
                ImageViewHolder(binding)
            }

            else -> {
                val binding = ItemLoadMoreBinding.inflate(inflater, parent, false)
                LoadMoreViewHolder(binding)
            }
        }
    }

    /** @see
     * 마지막으로 해당 화면에 갔을 때 데이터를 연결해 주기 위한 onBindViewHolder()
     * 1. currentList의 position을 통해 받는다면
     * val item = currentList[position]
     * when (item)
     * is ImageItems.Image -> {}
     * ImageItems.LoadMore -> {}
     * 위 방법은 compile 단계에서 else보다 명시적으로 확인 가능하기에 data class를 쓸 때에도 사용할 수 있고, 구현할 때 빠지면 안되는 부분이 있을 때 사용하면 좋다
     *
     * 2. Holder의 Type을 보고 판단
     * 마지막에 putter를 넣었고, putter를 넣었을 때에는 맨 마지막 Item은 currentList에 값이 없기 때문에 이 방법을 사용
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }

            is LoadMoreViewHolder -> {
                holder.bind(itemClickListener)
            }
        }
    }

    interface ItemClickListener {
        fun onLoadMoreClick()
    }

    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }
}

class ImageViewHolder(private val binding: ItemImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ImageItems.Image) {
        binding.previewImageView.setImageURI(item.uri)
    }
}

class LoadMoreViewHolder(binding: ItemLoadMoreBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(itemClickListener: ImageAdapter.ItemClickListener) {
        itemView.setOnClickListener { itemClickListener.onLoadMoreClick() }
    }
}