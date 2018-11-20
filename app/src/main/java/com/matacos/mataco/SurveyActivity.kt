package com.matacos.mataco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley

import kotlinx.android.synthetic.main.activity_survey.*
import org.json.JSONObject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SurveyActivity : AppCompatActivity() {

    private val TAG: String = SurveyActivity::class.java.simpleName

    var token = ""
    var student = ""
    var course = 0
    var generalOpinionText = 0
    var theoricalClassLevelText = 0
    var practicalClassLevelText = 0
    var courseDificultyText = 0
    var tpDificultyText = 0
    var interestingToppicsText = 0
    var updatedToppicsText = 0
    var aprovedCourseText = false
    var comentariesText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        val department: String = preferences.getString("subject_department", "")
        val code: String = preferences.getString("subject_code", "")
        val name: String = preferences.getString("subject_name", "")

        supportActionBar?.title = "$department.$code $name"

        setContentView(R.layout.activity_survey)

        send_survey_button.setOnClickListener {
            Log.d(TAG, "clicked send_survey_button")
            attemptSendingSurvey()
        }
    }

    private fun attemptSendingSurvey() {
        Log.d(TAG, "attemptLogin")


        general_opinion_title.error = null
        theorical_class_level_title.error = null
        practical_class_level_title.error = null
        course_dificulty_title.error = null
        tp_dificulty_title.error = null
        interesting_toppics_title.error = null
        updated_toppics_title.error = null
        aproved_course_title.error = null

        var cancel = false
        var focusView: View? = null

        if (general_opinion.getCheckedRadioButtonId() == -1) {
            general_opinion_title.error = getString(R.string.error_radio)
            focusView = general_opinion_title
            cancel = true
        }
        if (theorical_class_level.getCheckedRadioButtonId() == -1) {
            theorical_class_level_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = theorical_class_level_title
            cancel = true
        }
        if (practical_class_level.getCheckedRadioButtonId() == -1) {
            practical_class_level_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = practical_class_level_title
            cancel = true
        }
        if (course_dificulty.getCheckedRadioButtonId() == -1) {
            course_dificulty_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = course_dificulty_title
            cancel = true
        }
        if (tp_dificulty.getCheckedRadioButtonId() == -1) {
            tp_dificulty_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = tp_dificulty_title
            cancel = true
        }
        if (interesting_toppics.getCheckedRadioButtonId() == -1) {
            interesting_toppics_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = interesting_toppics_title
            cancel = true
        }
        if (updated_toppics.getCheckedRadioButtonId() == -1) {
            updated_toppics_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = updated_toppics_title
            cancel = true
        }
        if (aproved_course.getCheckedRadioButtonId() == -1) {
            aproved_course_title.error = getString(R.string.error_radio)
            if (focusView == null)
                focusView = aproved_course_title
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {

            getValuesToSend()

            val service = ServiceVolley()
            val apiController = APIController(service)

            val path = "api/poll"
            val params = JSONObject()
            params.put("course", course)
            params.put("student", student)
            params.put("q1", generalOpinionText)
            params.put("q2", theoricalClassLevelText)
            params.put("q3", practicalClassLevelText)
            params.put("q4", courseDificultyText)
            params.put("q5", tpDificultyText)
            params.put("q6", interestingToppicsText)
            params.put("q7", updatedToppicsText)
            params.put("passed", aprovedCourseText)
            params.put("feedback", comentariesText)

            Log.d(TAG, "params: $params")

            apiController.post(path, token, params) { response ->
                Log.d(TAG, "response: ${response.toString()}")
                if (response != null) {
                    Log.d(TAG, "startActivity: SurveySubjectsActivity")
                    Toast.makeText(this, "Se envió la encuesta correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SurveySubjectsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    this.startActivity(intent)

                } else {
                    Toast.makeText(this, "Error al enviar los datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getValuesToSend() {
        Log.d(TAG, "getValuesToSend")
        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        token = preferences.getString("token", "")
        Log.d(TAG, "token: $token")
        student = preferences.getString("username", "")
        Log.d(TAG, "student: $student")
        course = preferences.getInt("subject_course", 0)
        Log.d(TAG, "course: $course")

        var selectedId = general_opinion.getCheckedRadioButtonId()
        val generalOpinionSelection: RadioButton = findViewById(selectedId)!!
        generalOpinionText = Integer.parseInt(generalOpinionSelection.getText().toString())
        Log.d(TAG, "generalOpinionText: $generalOpinionText")

        selectedId = theorical_class_level.getCheckedRadioButtonId()
        val theoricalClassLevelSelection: RadioButton = findViewById(selectedId)!!
        theoricalClassLevelText = Integer.parseInt(theoricalClassLevelSelection.getText().toString())
        Log.d(TAG, "theoricalClassLevelText: $theoricalClassLevelText")

        selectedId = practical_class_level.getCheckedRadioButtonId()
        val practicalClassLevelSelection: RadioButton = findViewById(selectedId)!!
        practicalClassLevelText = Integer.parseInt(practicalClassLevelSelection.getText().toString())
        Log.d(TAG, "practicalClassLevelText: $practicalClassLevelText")

        selectedId = course_dificulty.getCheckedRadioButtonId()
        val courseDificultySelection: RadioButton = findViewById(selectedId)!!
        courseDificultyText = Integer.parseInt(courseDificultySelection.getText().toString())
        Log.d(TAG, "courseDificultyText: $courseDificultyText")

        selectedId = tp_dificulty.getCheckedRadioButtonId()
        val tpDificultySelection: RadioButton = findViewById(selectedId)!!
        tpDificultyText = Integer.parseInt(tpDificultySelection.getText().toString())
        Log.d(TAG, "tpDificultyText: $tpDificultyText")

        selectedId = interesting_toppics.getCheckedRadioButtonId()
        val interestingToppicsSelection: RadioButton = findViewById(selectedId)!!
        interestingToppicsText = Integer.parseInt(interestingToppicsSelection.getText().toString())
        Log.d(TAG, "interestingToppicsText: $interestingToppicsText")

        selectedId = updated_toppics.getCheckedRadioButtonId()
        val updatedToppicsSelection: RadioButton = findViewById(selectedId)!!
        updatedToppicsText = Integer.parseInt(updatedToppicsSelection.getText().toString())
        Log.d(TAG, "updatedToppicsText: $updatedToppicsText")

        selectedId = aproved_course.getCheckedRadioButtonId()
        val aprovedCourseSelection: RadioButton = findViewById(selectedId)!!
        aprovedCourseText = aprovedCourseSelection.text.toString() == "Si"
        Log.d(TAG, "aprovedCourseText: $aprovedCourseText")

        val comentaries: EditText = findViewById(R.id.comentaries)
        comentariesText = comentaries.text.toString()
        Log.d(TAG, "comentariesText: $comentariesText")
    }

}