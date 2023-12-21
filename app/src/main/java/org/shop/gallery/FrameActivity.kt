package org.shop.gallery

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import org.shop.gallery.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val images = (intent.getStringArrayExtra("images")
            ?: emptyArray()).map { uriString -> FrameItem(Uri.parse(uriString)) }

        val frameAdapter = FrameAdapter(images)

        binding.viewPager.adapter = frameAdapter

        /** @see
         *  이를 통해 ViewPager와 TabLayout을 연결하고 변경된 내용이 잘 연동될 수 있도록 작업
         */
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            binding.viewPager.currentItem = tab.position
        }.attach()
    }
}