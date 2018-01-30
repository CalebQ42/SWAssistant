package com.apps.darkstorm.swrpg.assistant.custVars

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.apps.darkstorm.swrpg.assistant.R
import org.jetbrains.anko.find
import org.jetbrains.anko.imageResource

class FloatingActionMenu(val root: ViewGroup) {
    val fab = LayoutInflater.from(root.context).inflate(R.layout.fam_main,root,true).find<FloatingActionButton>(R.id.fab)
    var items = mutableListOf<FloatingMenuItem>()
    var open: onOpenClose? = null
    var close: onOpenClose? = null
    var isMenu = false
    var isOpen = false
    var rotation: Float = 45f
    init{
        fab.visibility = View.GONE
    }
    fun setStatic(pictureResource: Int,click: ()->Unit){
        if(isMenu){
            removeMenu(true)
            isMenu = false
        }
        changeImage(pictureResource,click)
    }
    fun setMenu(menuItems: MutableList<FloatingMenuItem>,pictureResource: Int = R.drawable.add,rotationAmount: Float = 45f){
        if(menuItems.size>4)
            error("FAM can't be more then 4 items")
        if(isMenu)
            removeMenu(true)
        items = menuItems
        this.rotation = rotationAmount
        fab.hide(object: FloatingActionButton.OnVisibilityChangedListener(){
            override fun onHidden(fab: FloatingActionButton?) {
                changeImage(pictureResource,{openMenu()},{
                    for(i in items){
                        val v = LayoutInflater.from(root.context).inflate(R.layout.fam_item,root,false)
                        i.linkToItem(v,{closeMenu()})
                        root.addView(i.linkedItem)
                    }
                })
            }
        })
        isMenu = true
        isOpen = false
    }
    fun hide(){
        if(isMenu)
            removeMenu(true)
        isMenu = false
        fab.hide()
    }
    private fun changeImage(pictureResource: Int,click: () -> Unit,onShow: ()->Unit = {}){
        if(fab.visibility == View.GONE){
            fab.imageResource = pictureResource
            fab.setOnClickListener { click() }
            fab.show(object: FloatingActionButton.OnVisibilityChangedListener(){
                override fun onShown(fab: FloatingActionButton?) {
                    onShow()
                }
            })
        }else{
            fab.hide(object: FloatingActionButton.OnVisibilityChangedListener(){
                override fun onHidden(fab: FloatingActionButton?) {
                    fab?.imageResource = pictureResource
                    fab?.setOnClickListener { click() }
                    fab?.show(object: FloatingActionButton.OnVisibilityChangedListener(){
                        override fun onShown(fab: FloatingActionButton?) {
                            onShow()
                        }
                    })
                }
            })
        }
    }
    private fun removeMenu(wait: Boolean){
        var done = false
        if(isOpen){
            close = object: onOpenClose(){
                override fun onClose() {
                    for (i in items)
                        root.removeView(i.linkedItem)
                    items.clear()
                    done = true
                }
            }
            closeMenu()
            if(wait)
                while(!done)
                    Thread.sleep(300)
        }else {
            for (i in items)
                root.removeView(i.linkedItem)
            items.clear()
        }
    }
    private fun openMenu(){
        if(!isOpen) {
            fab.animate().rotation(rotation).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(p0: Animator?) {
                    fab.setOnClickListener {
                        closeMenu()
                    }
                }

                override fun onAnimationStart(p0: Animator?) {
                    fab.setOnClickListener {}
                }
            }).start()
            for ((i, it) in items.withIndex()) {
                when (i) {
                    0 -> {
                        if (items.size == i + 1)
                            it.linkedItem?.animate()?.translationYBy(-fab.resources.getDimension(R.dimen.fam_1))?.setListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(animation: Animator?) {}
                                override fun onAnimationEnd(animation: Animator?) {
                                    isOpen = true
                                    open?.onOpen()
                                }

                                override fun onAnimationCancel(animation: Animator?) {}
                                override fun onAnimationStart(animation: Animator?) {}
                            })?.start()
                        else
                            it.linkedItem?.animate()?.translationYBy(-fab.resources.getDimension(R.dimen.fam_1))?.start()
                    }
                    1 -> {
                        if (items.size == i + 1)
                            it.linkedItem?.animate()?.translationYBy(-fab.resources.getDimension(R.dimen.fam_2))?.setListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(animation: Animator?) {}
                                override fun onAnimationEnd(animation: Animator?) {
                                    isOpen = true
                                    open?.onOpen()
                                }

                                override fun onAnimationCancel(animation: Animator?) {}
                                override fun onAnimationStart(animation: Animator?) {}
                            })?.start()
                        else
                            it.linkedItem?.animate()?.translationYBy(-fab.resources.getDimension(R.dimen.fam_2))?.start()
                    }
                    2 -> {
                        if (items.size == i + 1)
                            it.linkedItem?.animate()?.translationYBy(-fab.resources.getDimension(R.dimen.fam_3))?.setListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(animation: Animator?) {}
                                override fun onAnimationEnd(animation: Animator?) {
                                    isOpen = true
                                    open?.onOpen()
                                }

                                override fun onAnimationCancel(animation: Animator?) {}
                                override fun onAnimationStart(animation: Animator?) {}
                            })?.start()
                        else
                            it.linkedItem?.animate()?.translationYBy(-fab.resources.getDimension(R.dimen.fam_3))?.start()
                    }
                    3 -> {
                        if (items.size == i + 1)
                            it.linkedItem?.animate()?.translationYBy(-fab.resources.getDimension(R.dimen.fam_4))?.setListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(animation: Animator?) {}
                                override fun onAnimationEnd(animation: Animator?) {
                                    isOpen = true
                                    open?.onOpen()
                                }

                                override fun onAnimationCancel(animation: Animator?) {}
                                override fun onAnimationStart(animation: Animator?) {}
                            })?.start()
                        else
                            it.linkedItem?.animate()?.translationYBy(-fab.resources.getDimension(R.dimen.fam_4))?.start()
                    }
                }
                if (it.getLabel() != "")
                    it.linkedItem?.find<TextView>(R.id.label)?.visibility = View.VISIBLE
            }
            val blocker = LayoutInflater.from(root.context).inflate(R.layout.fam_blocker,root,false)
            blocker.setOnClickListener { closeMenu() }
            root.addView(blocker, root.indexOfChild(root.find(R.id.content_main))+1)
            blocker.alpha = 0f
            blocker.animate().alpha(.5f)
        }
    }
    fun closeMenu(){
        if(isMenu && isOpen) {
            fab.animate().rotation(0f).setListener(object: AnimatorListenerAdapter(){
                override fun onAnimationEnd(p0: Animator?) {
                    fab.setOnClickListener {
                        openMenu()
                    }
                }
                override fun onAnimationStart(p0: Animator?) { fab.setOnClickListener {} }
            }).start()
            for ((i, it) in items.withIndex()) {
                when (i) {
                    0 -> {
                        if (items.size == i + 1)
                            it.linkedItem?.animate()?.translationYBy(fab.resources.getDimension(R.dimen.fam_1))?.setListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(animation: Animator?) {}
                                override fun onAnimationEnd(animation: Animator?) {
                                    isOpen = false
                                    close?.onClose()
                                }

                                override fun onAnimationCancel(animation: Animator?) {}
                                override fun onAnimationStart(animation: Animator?) {}
                            })?.start()
                        else
                            it.linkedItem?.animate()?.translationYBy(fab.resources.getDimension(R.dimen.fam_1))?.start()
                    }
                    1 -> {
                        if (items.size == i + 1)
                            it.linkedItem?.animate()?.translationYBy(fab.resources.getDimension(R.dimen.fam_2))?.setListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(animation: Animator?) {}
                                override fun onAnimationEnd(animation: Animator?) {
                                    isOpen = false
                                    close?.onClose()
                                }

                                override fun onAnimationCancel(animation: Animator?) {}
                                override fun onAnimationStart(animation: Animator?) {}
                            })?.start()
                        else
                            it.linkedItem?.animate()?.translationYBy(fab.resources.getDimension(R.dimen.fam_2))?.start()
                    }
                    2 -> {
                        if (items.size == i + 1)
                            it.linkedItem?.animate()?.translationYBy(fab.resources.getDimension(R.dimen.fam_3))?.setListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(animation: Animator?) {}
                                override fun onAnimationEnd(animation: Animator?) {
                                    isOpen = false
                                    close?.onClose()
                                }

                                override fun onAnimationCancel(animation: Animator?) {}
                                override fun onAnimationStart(animation: Animator?) {}
                            })?.start()
                        else
                            it.linkedItem?.animate()?.translationYBy(fab.resources.getDimension(R.dimen.fam_3))?.start()
                    }
                    3 -> {
                        if (items.size == i + 1)
                            it.linkedItem?.animate()?.translationYBy(fab.resources.getDimension(R.dimen.fam_4))?.setListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(animation: Animator?) {}
                                override fun onAnimationEnd(animation: Animator?) {
                                    isOpen = false
                                    close?.onClose()
                                }

                                override fun onAnimationCancel(animation: Animator?) {}
                                override fun onAnimationStart(animation: Animator?) {}
                            })?.start()
                        else
                            it.linkedItem?.animate()?.translationYBy(fab.resources.getDimension(R.dimen.fam_4))?.start()
                    }
                }
                it.linkedItem?.find<TextView>(R.id.label)?.visibility = View.GONE
                root.removeView(root.findViewById<FrameLayout>(R.id.fam_blocker))
            }
        }
    }
    abstract class onOpenClose{
        open fun onClose(){}
        open fun onOpen(){}
    }
    class FloatingMenuItem(var imageID: Int, var onClick: () -> Unit, private var label: String = ""){
        var linkedItem: View? = null
        lateinit var hideAction: ()->Unit
        fun setImage(imageID: Int){
            this.imageID = imageID
            if(linkedItem != null)
                linkedItem?.findViewById<FloatingActionButton>(R.id.item)?.imageResource = imageID
        }
        fun setOnClickListener(onClick: () -> Unit){
            this.onClick = onClick
            if(linkedItem != null)
                linkedItem?.find<FloatingActionButton>(R.id.item)?.setOnClickListener {
                    onClick()
                    hideAction()
                }
        }
        fun setLabel(label: String){
            this.label = label
            if(linkedItem != null) {
                if(label == "")
                    linkedItem?.find<TextView>(R.id.label)?.visibility = View.GONE
                else {
                    linkedItem?.find<TextView>(R.id.label)?.text = label
                    linkedItem?.find<TextView>(R.id.label)?.visibility = View.VISIBLE
                }
            }
        }
        fun getLabel() = label
        fun linkToItem(item: View, hideAction: ()->Unit){
            linkedItem = item
            this.hideAction = hideAction
            val fab = item.find<FloatingActionButton>(R.id.item)
            fab.setOnClickListener {
                onClick()
                hideAction()
            }
            fab.imageResource = imageID
            item.find<TextView>(R.id.label).text = label
        }
    }
}