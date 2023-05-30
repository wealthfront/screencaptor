package com.wealthfront.screencaptor.globalmutator

import android.app.Activity
import android.view.View
import com.wealthfront.screencaptor.GlobalViewMutation
import com.wealthfront.screencaptor.extensions.getAllChildren

internal class ViewTreeMutator(private val viewMutators: Set<GlobalViewMutation>) {

  fun mutate(activity: Activity) {
    mutate(activity.getRootViews())
  }

  fun mutate(views: List<View>) {
    views.forEach { view ->
      viewMutators.forEach { mutator ->
        val mutableChildren = view.getAllChildren().filter { view ->
          mutator.typesToMutate().any { it.isAssignableFrom(view::class.java) }
        }
        mutator.mutateView(mutableChildren)
      }
    }
  }

  internal class Builder {

    private var mutations: Set<GlobalViewMutation> = emptySet()

    fun addMutations(viewMutations: Set<GlobalViewMutation>) = apply {
      this.mutations = mutations.plus(viewMutations)
    }

    fun build(): ViewTreeMutator {
      return ViewTreeMutator(mutations)
    }
  }
}

private fun Activity.getRootViews(): List<View> {
  val globalWindowManagerField = windowManager.javaClass.getDeclaredField("mGlobal")
    .apply { isAccessible = true }
  val globalWindowManager = globalWindowManagerField.get(windowManager)
  val viewsField = globalWindowManager.javaClass.getDeclaredField("mViews")
    .apply { isAccessible = true }
  @Suppress("UNCHECKED_CAST")
  return viewsField.get(globalWindowManager) as List<View>
}
