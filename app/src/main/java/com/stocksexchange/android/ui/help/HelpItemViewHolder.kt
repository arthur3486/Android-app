package com.stocksexchange.android.ui.help

import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.stocksexchange.android.R
import com.stocksexchange.android.model.HelpItem
import com.stocksexchange.android.ui.base.adapters.recyclerview.BaseViewHolder
import com.stocksexchange.android.ui.utils.extensions.getColoredCompatDrawable
import net.cachapa.expandablelayout.ExpandableLayout

class HelpItemViewHolder(
    itemView: View,
    resources: HelpItemResources?
) : BaseViewHolder<HelpItem, HelpItemResources>(itemView, resources) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.help_item_layout

    }


    val mQuestionTv: TextView = itemView.findViewById(R.id.questionTv)
    val mAnswerTv: TextView = itemView.findViewById(R.id.answerTv)

    val mArrowIv: ImageView = itemView.findViewById(R.id.arrowIv)

    val mAnswerEl: ExpandableLayout = itemView.findViewById(R.id.answerEl)




    override fun bind(itemModel: HelpItem, resources: HelpItemResources?) {
        mQuestionTv.text = itemModel.question
        mAnswerTv.text = itemModel.answer
        mArrowIv.setImageDrawable(itemView.context.getColoredCompatDrawable(
            R.drawable.ic_menu_down,
            R.color.colorSecondaryText
        ))

        if(itemModel.isCollapsed()) {
            collapse(resources, false)
        } else {
            expand(resources, false)
        }
    }


    fun collapse(resources: HelpItemResources?, animate: Boolean = true) {
        if(animate) {
            mArrowIv.rotation = 180f
            mArrowIv.animate()
                .rotation(0f)
                .setInterpolator(LinearInterpolator())
                .setDuration(resources!!.animationDuration)
                .start()
            mAnswerEl.collapse()
        } else {
            mArrowIv.rotation = 0f
            mAnswerEl.collapse(false)
        }
    }


    fun expand(resources: HelpItemResources?, animate: Boolean = true) {
        if(animate) {
            mArrowIv.rotation = 0f
            mArrowIv.animate()
                .rotation(180f)
                .setInterpolator(LinearInterpolator())
                .setDuration(resources!!.animationDuration)
                .start()
            mAnswerEl.expand()
        } else {
            mArrowIv.rotation = 180f
            mAnswerEl.expand(false)
        }
    }


    fun setOnItemClickListener(position: Int, itemModel: HelpItem, listener: ((HelpItemViewHolder, HelpItem, Int) -> Unit)?) {
        itemView.setOnClickListener { listener?.invoke(this, itemModel, position) }
    }



}