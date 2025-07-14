package com.example.lucened;

public class LiKouDemo {

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("dvdf"));
    }


    /**
     * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
     * @param s
     * @return
     * */
    public static int lengthOfLongestSubstring(String s) {
        int max=0;
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<s.length();i++)
        {
          if(-1==sb.indexOf(String.valueOf(s.charAt(i))))//不包含
          {
              sb.append(s.charAt(i));
              max=Math.max(max,sb.length());
          }else{
              sb.delete(0,sb.indexOf(String.valueOf(s.charAt(i)))+1);//删除重复的
              sb.append(s.charAt(i));
          }
        }
        return max;
    }

    /**
     * 给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。
     * @param nums1
     * @param nums2
     * @return
     * */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
       double mid=0.0;
        int[] mergeTwoSortedArrays = mergeTwoSortedArrays(nums1, nums2);
        if(mergeTwoSortedArrays.length%2==0){
            mid=(mergeTwoSortedArrays[mergeTwoSortedArrays.length/2]+mergeTwoSortedArrays[mergeTwoSortedArrays.length/2-1])/2.0;
        }else{
            mid=mergeTwoSortedArrays[mergeTwoSortedArrays.length/2];
        }
        return mid;
    }

    public static int[] mergeTwoSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int[] result = new int[m + n];
        int i = 0, j = 0, k = 0;
        // 双指针比较合并
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                result[k++] = nums1[i++];
            } else {
                result[k++] = nums2[j++];
            }
        }
        // 复制 nums1 剩余元素
        while (i < m) {
            result[k++] = nums1[i++];
        }
        // 复制 nums2 剩余元素
        while (j < n) {
            result[k++] = nums2[j++];
        }
        return result;
    }

    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";

        // Step 1: 在原先字符串前后添加特殊字符，以处理边界情况
        StringBuilder t = new StringBuilder("#");
        for (char c : s.toCharArray()) {
            t.append(c).append("#");
        }
        int n = t.length();
        int[] p = new int[n];
        int center = 0, right = 0;
        int maxLen = 0, maxCenter = 0;
        // Step 2 & 3: Process each character in transformed string
        for (int i = 0; i < n; i++) {
            int mirror = 2 * center - i; // Mirror of current index i
            if (i < right) {
                p[i] = Math.min(right - i, p[mirror]);
            }
            // Attempt to expand palindrome centered at i
            int a = i + (1 + p[i]);
            int b = i - (1 + p[i]);
            while (a < n && b >= 0 && t.charAt(a) == t.charAt(b)) {
                p[i]++;
                a++;
                b--;
            }
            // Update center and right boundary if necessary
            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
                if (p[i] > maxLen) {
                    maxLen = p[i];
                    maxCenter = i;
                }
            }
        }
        // Step 4: Extract the longest palindromic substring
        int start = (maxCenter - maxLen) / 2; // Adjust for added '#' characters
        return s.substring(start, start + maxLen);
    }
}
