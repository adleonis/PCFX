package org.pcfx.pdv.androidpdv1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.pcfx.pdv.androidpdv1.R
import org.pcfx.pdv.androidpdv1.service.PdvServerService
import com.google.gson.Gson
import java.net.URL

class DashboardFragment : Fragment() {
    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.Main)
    private var serverPort = 7777

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scope.launch {
            while (true) {
                loadStatistics()
                delay(3000) // Refresh every 3 seconds
            }
        }
    }

    private suspend fun loadStatistics() {
        try {
            val url = "http://127.0.0.1:$serverPort/stats"
            val response = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                URL(url).readText()
            }
            val statsMap = gson.fromJson(response, Map::class.java) as? Map<String, Any>

            if (statsMap != null) {
                updateUI(statsMap)
            }
        } catch (e: Exception) {
            // Stats endpoint may not be available, show defaults
        }
    }

    private fun updateUI(stats: Map<String, Any>) {
        val view = view ?: return

        // Events, Atoms, Metrics
        val eventsValue = (stats["events"] as? Number)?.toInt() ?: 0
        val atomsValue = (stats["atoms"] as? Number)?.toInt() ?: 0
        val metricsValue = (stats["metrics"] as? Number)?.toInt() ?: 0

        view.findViewById<TextView>(R.id.eventsValue)?.text = eventsValue.toString()
        view.findViewById<TextView>(R.id.atomsValue)?.text = atomsValue.toString()
        view.findViewById<TextView>(R.id.metricsValue)?.text = metricsValue.toString()

        // Adapters
        val adaptersMap = stats["adapters"] as? Map<String, Any>
        if (adaptersMap != null) {
            view.findViewById<TextView>(R.id.adaptersTotalValue)?.text =
                (adaptersMap["total"] as? Number)?.toInt()?.toString() ?: "0"
            view.findViewById<TextView>(R.id.adapters24hValue)?.text =
                (adaptersMap["active_24h"] as? Number)?.toInt()?.toString() ?: "0"
        }

        // Nodes
        val nodesMap = stats["nodes"] as? Map<String, Any>
        if (nodesMap != null) {
            view.findViewById<TextView>(R.id.nodesTotalValue)?.text =
                (nodesMap["total"] as? Number)?.toInt()?.toString() ?: "0"
            view.findViewById<TextView>(R.id.nodes24hValue)?.text =
                (nodesMap["active_24h"] as? Number)?.toInt()?.toString() ?: "0"
        }

        // Clients
        val clientsMap = stats["clients"] as? Map<String, Any>
        if (clientsMap != null) {
            view.findViewById<TextView>(R.id.clientsTotalValue)?.text =
                (clientsMap["total"] as? Number)?.toInt()?.toString() ?: "0"
            view.findViewById<TextView>(R.id.clients24hValue)?.text =
                (clientsMap["active_24h"] as? Number)?.toInt()?.toString() ?: "0"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.launch { }
    }
}
