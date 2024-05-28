package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.view.AuthViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signUpViewModel: SignUpViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
        setupTextWatchers()
        showLoading(false)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupTextWatchers(){
        with(binding){
            passwordEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().length < 8) {
                        binding.passwordEditText.setError(
                            "Password tidak boleh kurang dari 8 karakter",
                            null
                        )
                    } else {
                        binding.passwordEditText.error = null
                    }
                }
                override fun afterTextChanged(p0: Editable?) {}
            })
            emailEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()){
                        binding.emailEditText.setError(
                            "Format email salah",
                            null
                        )
                    }else{
                        binding.emailEditText.error = null
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                showLoading(true)
                Log.d("SignUpActivity", "Registering with name: $name, email: $email")
                signUpViewModel.register(name, email, password).observe(this) { result ->
                    showLoading(false)
                    result.onSuccess { registerResponse ->
                        onRegisterSuccess("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                    }
                    result.onFailure { exception ->
                        onRegisterError("Terjadi kesalahan: ${exception.message}")
                    }
                }
            } else {
                onRegisterError("Semua field harus diisi")
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }

    private fun onRegisterSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    private fun onRegisterError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        AlertDialog.Builder(this).apply {
            setTitle("Register Failed")
            setMessage(message)
            setPositiveButton("Coba Lagi") { _, _ -> }
            create()
            show()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBarRegis.visibility = if (state) View.VISIBLE else View.GONE
    }
}