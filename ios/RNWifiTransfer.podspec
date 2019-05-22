
Pod::Spec.new do |s|
  s.name         = "RNWifiTransfer"
  s.version      = "1.0.0"
  s.summary      = "RNWifiTransfer"
  s.description  = <<-DESC
                  RNWifiTransfer
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNWifiTransfer.git", :tag => "master" }
  s.source_files  = "RNWifiTransfer/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  