package com.wealthfront.screencaptor.views.mutator

import android.view.View
import com.wealthfront.screencaptor.views.extensions.getAllChildren

internal class ViewTreeMutator private constructor() {

  internal fun mutateView(viewToBeMutated: View, viewMutators: Set<ViewMutator>) {
    viewMutators.forEach { mutator ->
      val mutableChildren = viewToBeMutated.getAllChildren().filter { view ->
        mutator.typesToMutate().any { it.isAssignableFrom(view::class.java) }
      }
      mutator.mutateView(mutableChildren)
    }
  }

  internal class Builder {

    private lateinit var viewMutator: Set<ViewMutator>
    private lateinit var viewToBeMutated: View

    fun viewMutators(viewMutator: Set<ViewMutator>) = apply {
      this.viewMutator = viewMutator
    }

    fun mutateView(view: View) = apply {
      this.viewToBeMutated = view
    }

    fun mutate() {
      val mutator = ViewTreeMutator()
      mutator.mutateView(viewToBeMutated, viewMutator)
    }
  }
}
