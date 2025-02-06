package com.github.yorik56.gitmojicommitcolumn

import com.intellij.vcs.log.VcsCommitMetadata
import com.intellij.vcs.log.ui.table.GraphTableModel
import com.intellij.vcs.log.ui.table.VcsLogGraphTable
import com.intellij.vcs.log.ui.table.column.VcsLogCustomColumn
import javax.swing.JLabel
import javax.swing.table.TableCellRenderer

class GitmojiCustomColumn : VcsLogCustomColumn<String> {

    override val id: String
        get() = "gitmoji.custom.column"

    override val isDynamic: Boolean
        get() = false

    override val localizedName: String
        get() = "Gitmoji"

    override fun getValue(model: GraphTableModel, row: Int): String? {
        return try {
            val commit = model.getCommitMetadata(row)
            commit?.let { convertCommitMessage(it.subject) }
        } catch (e: Exception) {
            null
        }
    }

    override fun createTableCellRenderer(table: VcsLogGraphTable): TableCellRenderer {
        return TableCellRenderer { _, value, _, _, _, _ ->
            JLabel(value as? String ?: "")
        }
    }

    override fun getStubValue(model: GraphTableModel): String {
        return ""
    }

    /**
     * 1. Extrait tous les codes :xxxx: du message
     * 2. Ne remplace que ceux présents dans le mapping par l'émoji correspondant
     * 3. Concatène tous les émojis trouvés et supprime le reste
     * 4. Tronque la chaîne finale à 50 caractères si besoin
     */
    private fun convertCommitMessage(message: String): String {
        // Regex qui capture toutes les séquences `:quelquechose:`
        val codeRegex = Regex(":[^:]+:")

        // On reconstruit un nouveau texte ne contenant QUE les émojis reconnus
        val sb = StringBuilder()
        codeRegex.findAll(message).forEach { match ->
            val code = match.value // ex. ":bug:"
            mapping[code]?.let { emoji ->
                sb.append(emoji) // On garde seulement les émojis connus
            }
        }

        // Tronquer si plus de 50 caractères
        return sb.toString().let { emojis ->
            if (emojis.length > 50) emojis.substring(0, 50) + "..." else emojis
        }
    }

    /**
     * Liste de correspondance "code Gitmoji" -> émoji.
     * Seuls ces codes sont remplacés, le reste (texte, codes non reconnus...)
     * est totalement ignoré (supprimé) dans le résultat final.
     */
    private val mapping = mapOf(
        ":art:" to "🎨",
        ":zap:" to "⚡️",
        ":fire:" to "🔥",
        ":bug:" to "🐛",
        ":ambulance:" to "🚑️",
        ":sparkles:" to "✨",
        ":memo:" to "📝",
        ":rocket:" to "🚀",
        ":lipstick:" to "💄",
        ":tada:" to "🎉",
        ":white_check_mark:" to "✅",
        ":lock:" to "🔒️",
        ":closed_lock_with_key:" to "🔐",
        ":bookmark:" to "🔖",
        ":rotating_light:" to "🚨",
        ":construction:" to "🚧",
        ":green_heart:" to "💚",
        ":arrow_down:" to "⬇️",
        ":arrow_up:" to "⬆️",
        ":pushpin:" to "📌",
        ":construction_worker:" to "👷",
        ":chart_with_upwards_trend:" to "📈",
        ":recycle:" to "♻️",
        ":heavy_plus_sign:" to "➕",
        ":heavy_minus_sign:" to "➖",
        ":wrench:" to "🔧",
        ":hammer:" to "🔨",
        ":globe_with_meridians:" to "🌐",
        ":pencil2:" to "✏️",
        ":poop:" to "💩",
        ":rewind:" to "⏪️",
        ":twisted_rightwards_arrows:" to "🔀",
        ":package:" to "📦️",
        ":alien:" to "👽️",
        ":truck:" to "🚚",
        ":page_facing_up:" to "📄",
        ":boom:" to "💥",
        ":bento:" to "🍱",
        ":wheelchair:" to "♿️",
        ":bulb:" to "💡",
        ":beers:" to "🍻",
        ":speech_balloon:" to "💬",
        ":card_file_box:" to "🗃️",
        ":loud_sound:" to "🔊",
        ":mute:" to "🔇",
        ":busts_in_silhouette:" to "👥",
        ":children_crossing:" to "🚸",
        ":building_construction:" to "🏗️",
        ":iphone:" to "📱",
        ":clown_face:" to "🤡",
        ":egg:" to "🥚",
        ":see_no_evil:" to "🙈",
        ":camera_flash:" to "📸",
        ":alembic:" to "⚗️",
        ":mag:" to "🔍️",
        ":label:" to "🏷️",
        ":seedling:" to "🌱",
        ":triangular_flag_on_post:" to "🚩",
        ":goal_net:" to "🥅",
        ":dizzy:" to "💫",
        ":wastebasket:" to "🗑️",
        ":passport_control:" to "🛂",
        ":adhesive_bandage:" to "🩹",
        ":monocle_face:" to "🧐",
        ":coffin:" to "⚰️",
        ":test_tube:" to "🧪",
        ":necktie:" to "👔",
        ":stethoscope:" to "🩺",
        ":bricks:" to "🧱",
        ":technologist:" to "🧑‍💻",
        ":money_with_wings:" to "💸",
        ":thread:" to "🧵",
        ":safety_vest:" to "🦺"
    )
}
